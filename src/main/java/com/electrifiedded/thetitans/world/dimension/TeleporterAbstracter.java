package com.electrifiedded.thetitans.world.dimension;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class TeleporterAbstracter extends Teleporter {
    private final WorldServer serverInstanceRealm;
    /** A private Random() function in Teleporter */
    private final Random random;
    /** Stores successful portal placement locations for rapid lookup. */
    private final Long2ObjectMap<Teleporter.PortalPosition> destinationCoordinateCache = new Long2ObjectOpenHashMap<Teleporter.PortalPosition>(4096);
    /**
     * A list of valid keys for the destinationCoordainteCache. These are based on the X & Z of the players initial
     * location.
     */
    private final List<Long> coordinateKeysDestination = new ArrayList<Long>();

    public TeleporterAbstracter(WorldServer par1WorldServer) {
        super(par1WorldServer);
        this.serverInstanceRealm = par1WorldServer;
        this.random = new Random(par1WorldServer.getSeed());
    }

    /**
     * Place an entity in a nearby portal, creating one if necessary.
     * 1.12.2 signature: placeInPortal(Entity, float). The old (x,y,z) coords are taken from the entity.
     */
    public void placeInPortal(Entity par1Entity, float par8) {
        if (this.serverInstanceRealm.provider.getDimension() != 1) {
            if (!this.placeInExistingPortal(par1Entity, par8)) {
                this.makePortal(par1Entity);
                this.placeInExistingPortal(par1Entity, par8);
            }
        }

        else {
            int i = MathHelper.floor(par1Entity.posX);
            int j = MathHelper.floor(par1Entity.posY) - 1;
            int k = MathHelper.floor(par1Entity.posZ);
            for (int l = -2; l <= 2; ++l) {
                for (int i1 = -2; i1 <= 2; ++i1) {
                    for (int j1 = -1; j1 < 3; ++j1) {
                    }
                }
            }

            par1Entity.setLocationAndAngles((double)i, (double)j, (double)k, par1Entity.rotationYaw, 0.0F);
            par1Entity.motionX = par1Entity.motionY = par1Entity.motionZ = 0.0D;
        }
    }

    /**
     * Place an entity in a nearby portal which already exists.
     */
    public boolean placeInExistingPortal(Entity par1Entity, float par8) {
        short short1 = 128;
        double d3 = -1.0D;
        int i = 0;
        int j = 0;
        int k = 0;
        int l = MathHelper.floor(par1Entity.posX);
        int i1 = MathHelper.floor(par1Entity.posZ);
        long j1 = ChunkPos.asLong(l, i1);
        boolean flag = true;
        double d4;
        int k1;
        if (this.destinationCoordinateCache.containsKey(j1)) {
            Teleporter.PortalPosition portalposition = this.destinationCoordinateCache.get(j1);
            d3 = 0.0D;
            i = portalposition.getX();
            j = portalposition.getY();
            k = portalposition.getZ();
            portalposition.lastUpdateTime = this.serverInstanceRealm.getTotalWorldTime();
            flag = false;
        }

        else {
            for (k1 = l - short1; k1 <= l + short1; ++k1) {
                for (int l1 = i1 - short1; l1 <= i1 + short1; ++l1) {
                    for (int i2 = this.serverInstanceRealm.getActualHeight() - 1; i2 >= 0; --i2) {
                    }
                }
            }
        }

        if (d3 >= 0.0D) {
            if (flag) {
                this.destinationCoordinateCache.put(j1, new Teleporter.PortalPosition(new BlockPos(i, j, k), this.serverInstanceRealm.getTotalWorldTime()));
                this.coordinateKeysDestination.add(Long.valueOf(j1));
            }

            double d8 = (double)i + 0.5D;
            double d9 = (double)j + 0.5D;
            d4 = (double)k + 0.5D;
            int j2 = -1;
            if (j2 > -1) {
                // unreachable ; orientation logic dropped.
            }

            else {
                par1Entity.motionX = par1Entity.motionY = par1Entity.motionZ = 0.0D;
            }

            par1Entity.setLocationAndAngles(d8, d9, d4, par1Entity.rotationYaw, par1Entity.rotationPitch);
            return true;
        }

        else {
            return false;
        }
    }

    public boolean makePortal(Entity par1Entity) {
        byte b0 = 16;
        double d0 = -1.0D;
        int i = MathHelper.floor(par1Entity.posX);
        int j = MathHelper.floor(par1Entity.posY);
        int k = MathHelper.floor(par1Entity.posZ);
        int l = i;
        int i1 = j;
        int j1 = k;
        int k1 = 0;
        int l1 = this.random.nextInt(4);
        int i2;
        double d1;
        double d2;
        int j2;
        int k2;
        int l2;
        int i3;
        int j3;
        int k3;
        int l3;
        int i4;
        int j4;
        int k4;
        double d3;
        double d4;
        for (i2 = i - b0; i2 <= i + b0; ++i2) {
            d1 = (double)i2 + 0.5D - par1Entity.posX;
            for (j2 = k - b0; j2 <= k + b0; ++j2) {
                d2 = (double)j2 + 0.5D - par1Entity.posZ;
                label274:
                for (k2 = this.serverInstanceRealm.getActualHeight() - 1; k2 >= 0; --k2) {
                    if (this.serverInstanceRealm.isAirBlock(new BlockPos(i2, k2, j2))) {
                        while (k2 > 0 && this.serverInstanceRealm.isAirBlock(new BlockPos(i2, k2 - 1, j2))) {
                            --k2;
                        }

                        for (i3 = l1; i3 < l1 + 4; ++i3) {
                            l2 = i3 % 2;
                            k3 = 1 - l2;
                            if (i3 % 4 >= 2) {
                                l2 = -l2;
                                k3 = -k3;
                            }

                            for (j3 = 0; j3 < 3; ++j3) {
                                for (i4 = 0; i4 < 4; ++i4) {
                                    for (l3 = -1; l3 < 4; ++l3) {
                                        k4 = i2 + (i4 - 1) * l2 + j3 * k3;
                                        j4 = k2 + l3;
                                        int l4 = j2 + (i4 - 1) * k3 - j3 * l2;
                                        if (l3 < 0 && !this.serverInstanceRealm.getBlockState(new BlockPos(k4, j4, l4)).getMaterial().isSolid() || l3 >= 0 && !this.serverInstanceRealm.isAirBlock(new BlockPos(k4, j4, l4))) {
                                            continue label274;
                                        }
                                    }
                                }
                            }

                            d4 = (double)k2 + 0.5D - par1Entity.posY;
                            d3 = d1 * d1 + d4 * d4 + d2 * d2;
                            if (d0 < 0.0D || d3 < d0) {
                                d0 = d3;
                                l = i2;
                                i1 = k2;
                                j1 = j2;
                                k1 = i3 % 4;
                            }
                        }
                    }
                }
            }
        }

        if (d0 < 0.0D) {
            for (i2 = i - b0; i2 <= i + b0; ++i2) {
                d1 = (double)i2 + 0.5D - par1Entity.posX;
                for (j2 = k - b0; j2 <= k + b0; ++j2) {
                    d2 = (double)j2 + 0.5D - par1Entity.posZ;
                    label222:
                    for (k2 = this.serverInstanceRealm.getActualHeight() - 1; k2 >= 0; --k2) {
                        if (this.serverInstanceRealm.isAirBlock(new BlockPos(i2, k2, j2))) {
                            while (k2 > 0 && this.serverInstanceRealm.isAirBlock(new BlockPos(i2, k2 - 1, j2))) {
                                --k2;
                            }

                            for (i3 = l1; i3 < l1 + 2; ++i3) {
                                l2 = i3 % 2;
                                k3 = 1 - l2;
                                for (j3 = 0; j3 < 4; ++j3) {
                                    for (i4 = -1; i4 < 4; ++i4) {
                                        l3 = i2 + (j3 - 1) * l2;
                                        k4 = k2 + i4;
                                        j4 = j2 + (j3 - 1) * k3;
                                        if (i4 < 0 && !this.serverInstanceRealm.getBlockState(new BlockPos(l3, k4, j4)).getMaterial().isSolid() || i4 >= 0 && !this.serverInstanceRealm.isAirBlock(new BlockPos(l3, k4, j4))) {
                                            continue label222;
                                        }
                                    }
                                }

                                d4 = (double)k2 + 0.5D - par1Entity.posY;
                                d3 = d1 * d1 + d4 * d4 + d2 * d2;
                                if (d0 < 0.0D || d3 < d0) {
                                    d0 = d3;
                                    l = i2;
                                    i1 = k2;
                                    j1 = j2;
                                    k1 = i3 % 2;
                                }
                            }
                        }
                    }
                }
            }
        }

        int i5 = l;
        int j5 = i1;
        j2 = j1;
        int k5 = k1 % 2;
        int l5 = 1 - k5;
        if (k1 % 4 >= 2) {
            k5 = -k5;
            l5 = -l5;
        }

        if (d0 < 0.0D) {
            if (i1 < 70) {
                i1 = 70;
            }

            if (i1 > this.serverInstanceRealm.getActualHeight() - 10) {
                i1 = this.serverInstanceRealm.getActualHeight() - 10;
            }

            j5 = i1;
            for (k2 = -1; k2 <= 1; ++k2) {
                for (i3 = 1; i3 < 3; ++i3) {
                    for (l2 = -1; l2 < 3; ++l2) {
                        k3 = i5 + (i3 - 1) * k5 + k2 * l5;
                        j3 = j5 + l2;
                        i4 = j2 + (i3 - 1) * l5 - k2 * k5;
                    }
                }
            }
        }

        for (k2 = 0; k2 < 4; ++k2) {
            for (i3 = 0; i3 < 4; ++i3) {
                for (l2 = -1; l2 < 4; ++l2) {
                    k3 = i5 + (i3 - 1) * k5;
                    j3 = j5 + l2;
                    i4 = j2 + (i3 - 1) * l5;
                }
            }

            for (i3 = 0; i3 < 4; ++i3) {
                for (l2 = -1; l2 < 4; ++l2) {
                    k3 = i5 + (i3 - 1) * k5;
                    j3 = j5 + l2;
                    i4 = j2 + (i3 - 1) * l5;
                }
            }
        }

        return true;
    }

    /**
     * called periodically to remove out-of-date portal locations from the cache list. Argument par1 is a
     * WorldServer.getTotalWorldTime() value.
     */
    public void removeStalePortalLocations(long par1) {
        if (par1 % 100L == 0L) {
            Iterator<Long> iterator = this.coordinateKeysDestination.iterator();
            long j = par1 - 600L;
            while (iterator.hasNext()) {
                Long olong = (Long)iterator.next();
                Teleporter.PortalPosition portalposition = this.destinationCoordinateCache.get(olong.longValue());
                if (portalposition == null || portalposition.lastUpdateTime < j) {
                    iterator.remove();
                    this.destinationCoordinateCache.remove(olong.longValue());
                }
            }
        }
    }
}
