package witchinggadgets.common.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemSnowball;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathEntity;

public class EntityAIWeaponAttack extends EntityAIBase
{
    public final EntityScarecrow scarecrow;
    public final double moveSpeed;
    public EntityLivingBase target;
    public PathEntity path = null;
    public int pathDelay = 0;
    public int sightTime = 0;
    public boolean avoidsWater;

    public int rodTime = 0;

    public EntityAIWeaponAttack(EntityScarecrow entity, double speed) {
        this.scarecrow = entity;
        this.moveSpeed = speed;
        this.setMutexBits(3);
    }

    @Override
    public boolean shouldExecute() {
        EntityLivingBase entity = this.scarecrow.getAttackTarget();
        if (entity == null)
            return false;
        this.target = entity;
        ItemStack weapon = this.scarecrow.getEquipmentInSlot(0);
        if (weapon != null && (this.isRangedWeapon(weapon))) // || weapon.getItem() instanceof ItemFishingRod))
            return true;
        this.path = this.scarecrow.getNavigator().getPathToEntityLiving(this.target);
        return this.path != null;
    }

    @Override
    public boolean continueExecuting() {
        return this.scarecrow.getRNG().nextInt(200) != 0 &&  (!this.scarecrow.getNavigator().noPath() || this.isRangedWeapon(this.scarecrow.getEquipmentInSlot(0)));
    }

    @Override
    public void startExecuting() {
        this.pathDelay = 0;
        this.avoidsWater = this.scarecrow.getNavigator().getAvoidsWater();
        this.scarecrow.getNavigator().setAvoidsWater(false);
        if (!this.isRangedWeapon(this.scarecrow.getEquipmentInSlot(0))) {
            this.scarecrow.getNavigator().setPath(this.path, this.moveSpeed);
        }
    }

    @Override
    public void resetTask() {
        this.target = null;
        this.sightTime = 0;
        this.scarecrow.setAttackTarget(null);
        this.scarecrow.getNavigator().setAvoidsWater(this.avoidsWater);
        this.scarecrow.getNavigator().clearPathEntity();
    }

    @Override
    public void updateTask() {
        ItemStack weapon = this.scarecrow.getEquipmentInSlot(0);
        this.scarecrow.getLookHelper().setLookPositionWithEntity(this.target, 30.0F, 30.0F);
        if (this.isRangedWeapon(weapon)) {
            double distanceSq = this.scarecrow.getDistanceSqToEntity(this.target);
            boolean canSee = this.scarecrow.getEntitySenses().canSee(this.target);
            if (distanceSq <= 144.0 && canSee) {
                this.sightTime++;
            }
            else {
                this.sightTime = 0;
            }
            if (this.sightTime < 20) {
                this.scarecrow.getNavigator().tryMoveToEntityLiving(this.target, this.moveSpeed);
            }
            else {
                this.scarecrow.getNavigator().clearPathEntity();
            }
            if (this.scarecrow.scarecrowAttackTime > 0)
                return;
            if (distanceSq > 144.0 || !canSee)
                return;
            this.scarecrow.doRangedAttack(this.target);
            Item item = weapon.getItem();
            if (item instanceof ItemBow) {
                this.scarecrow.scarecrowAttackTime = 60;
            }
            else {
                this.scarecrow.scarecrowAttackTime = 20;
            }
        }
        else {
            if (this.scarecrow.getEntitySenses().canSee(this.target) && --this.pathDelay <= 0) {
                this.pathDelay = 4 + this.scarecrow.getRNG().nextInt(7);
                this.scarecrow.getNavigator().tryMoveToEntityLiving(this.target, this.moveSpeed);
            }
            double reach = this.scarecrow.width * this.scarecrow.width * 4.0F + this.scarecrow.width;
            if (this.scarecrow.getDistanceSq(this.target.posX, this.target.boundingBox.minY, this.target.posZ) <= reach) {
                if (this.scarecrow.scarecrowAttackTime <= 0) {
                    this.scarecrow.scarecrowAttackTime = 20;
                    this.scarecrow.swingItem();
                    this.scarecrow.attackEntityAsMob(this.target);
                }
            }
        }
    }

    public boolean isRangedWeapon(ItemStack itemStack) {
        return itemStack != null && (itemStack.getItem() instanceof ItemBow || itemStack.getItem() instanceof ItemSnowball);
    }
}