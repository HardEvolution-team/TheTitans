import socket, struct, sys, time
def pkt(s, i, t, body):
    b = body.encode() + b'\x00\x00'
    s.sendall(struct.pack('<iii', len(b) + 8, i, t) + b)
def recv(s):
    n = struct.unpack('<i', s.recv(4))[0]
    d = b''
    while len(d) < n: d += s.recv(n - len(d))
    return d[8:-2].decode(errors='replace')
s = socket.create_connection(('127.0.0.1', int(sys.argv[1])))
pkt(s, 1, 3, 'bench'); recv(s)
for c in sys.argv[2:]:
    pkt(s, 2, 2, c); print(recv(s))
