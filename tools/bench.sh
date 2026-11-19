#!/usr/bin/env bash
# Titan perf harness: builds, starts a dev server (flat world, RCON 25592), runs /titanbench for each mode given,
# prints the [TitanBench] report lines and stops the server. Usage: tools/bench.sh [ticks] [copies] [modes...]
set -u
cd "$(dirname "$0")/.."
TICKS=${1:-400}; COPIES=${2:-1}; shift 2 2>/dev/null; MODES=${@:-dormant awake}
LOG=build/bench-server.log
./gradlew build -x test -q || exit 1
rm -rf run/server/benchworld
(sleep 3000 | ./gradlew runServer > "$LOG" 2>&1) &
for i in $(seq 1 600); do grep -q "RCON running" "$LOG" 2>/dev/null && break; grep -qE "FAILED TO BIND|BUILD FAILED" "$LOG" 2>/dev/null && { echo "server failed to start"; exit 1; }; sleep 1; done
for m in $MODES; do
  n=$(grep -c "\] done" "$LOG")
  python tools/rcon.py 25592 "titanbench $m $TICKS $COPIES" >/dev/null
  for i in $(seq 1 900); do [ "$(grep -c "\] done" "$LOG")" -gt "$n" ] && break; grep -q "ServerHangWatchdog" "$LOG" && break; sleep 1; done
done
grep -E "\[TitanBench\]|ServerHangWatchdog|Can't keep up" "$LOG" | sed 's/.*\]: //'
python tools/rcon.py 25592 stop >/dev/null 2>&1; sleep 5
