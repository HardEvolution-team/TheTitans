package com.electrifiedded.thetitans.entity.titan;

// mod's attack filter, split off vanilla canAttackClass (erasure clash in 1.12.2).
// Lets call sites invoke allowStrikeGiant on titan/minion shooters typed as vanilla EntityLiving.
public interface ICanAttack {
    boolean allowStrikeGiant(Class<?> clazz);
}
