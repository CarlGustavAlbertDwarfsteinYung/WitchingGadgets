package witchinggadgets.common.entity;

import net.minecraft.command.IEntitySelector;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.init.Blocks;
import net.minecraft.item.*;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.lwjgl.Sys;

public class EntityScarecrow extends EntityGolem implements IEntitySelector {
    /// The texture for this class.
    public static final ResourceLocation TEXTURE = new ResourceLocation("witchinggadgets:textures/entity/scarecrow.png");
    /// Attack time counter.
    public int scarecrowAttackTime = 0;
    /// Whether the scarecrow sinks in water. Also doubles as another inWater flag.
    public byte sinks = -1;

    public EntityScarecrow(World world) {
        super(world);
        this.getNavigator().setAvoidsWater(true);
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.targetTasks.addTask(0, new EntityAIHurtByTarget(this, false));
        this.tasks.addTask(1, new EntityAIWeaponAttack(this, 1.0));
        this.tasks.addTask(3, new EntityAIWander(this, 1.0));
        this.tasks.addTask(4, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(4, new EntityAILookIdle(this));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, IMob.class, 0, true));

    }

    /// Used to initialize data watcher variables.
    @Override
    protected void entityInit() {
        super.entityInit();
        /// owner; The username of this scarecrow's creator.
        this.dataWatcher.addObject(17, "");
    }

    /// Get/set functions for the owner name.
    public String getCreator() {
        return this.dataWatcher.getWatchableObjectString(17);
    }

    public void setCreator(String username) {
        if (!this.getCreator().equals(username)) {
            this.dataWatcher.updateObject(17, username == null ? "" : username);
        }
    }

    /// Initializes this entity's attributes.
    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(4.0);
        this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(16.0);
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(20.0);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.25);
    }

    @Override
    public boolean isAIEnabled() {
        return true;
    }

    /// Returns the texture for this mob.
    public ResourceLocation getTexture() {
        return TEXTURE;
    }

    @Override
    public void onUpdate() {
        this.scarecrowAttackTime = Math.max(this.scarecrowAttackTime - 1, 0);
        super.onUpdate();
    }

    /// Returns the heldItem.
    @Override
    public ItemStack getHeldItem() {
        return super.getHeldItem();
    }

    @Override
    public boolean attackEntityAsMob(Entity entity) {
        float attackDamage = (float)this.getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue();
        int knockback = 0;
        if (entity instanceof EntityLivingBase) {
            attackDamage += EnchantmentHelper.getEnchantmentModifierLiving(this, (EntityLivingBase)entity);
            knockback += EnchantmentHelper.getKnockbackModifier(this, (EntityLivingBase)entity);
        }

        boolean hit = entity.attackEntityFrom(DamageSource.causeMobDamage(this), attackDamage);
        if (hit) {

            if (knockback > 0) {
                entity.addVelocity(-MathHelper.sin(this.rotationYaw * (float)Math.PI / 180.0F) * knockback * 0.5F, 0.1, MathHelper.cos(this.rotationYaw * (float)Math.PI / 180.0F) * knockback * 0.5F);
                this.motionX *= 0.6;
                this.motionZ *= 0.6;
            }

            if (entity instanceof EntityLivingBase) {
                EnchantmentHelper.func_151384_a((EntityLivingBase) entity, this); // Triggers hit entity's enchants.
            }
            EnchantmentHelper.func_151385_b(this, entity); // Triggers attacker's enchants.
        }
        return hit;
    }

    // Returns the sound this mob makes while it's alive.
    @Override
    protected String getLivingSound() {
        return null; // Overridden to stop scarecrows from spamming the console.
    }

    // Returns the sound this mob makes when it is hurt.
    @Override
    protected String getHurtSound() {
        return null; // Overridden to stop scarecrows from spamming the console.
    }

    // Returns the sound this mob makes on death.
    @Override
    protected String getDeathSound() {
        return null; // Overridden to stop scarecrows from spamming the console.
    }

    @Override
    protected void dropFewItems(boolean recentlyHit, int looting) {
        this.dropFewItems(recentlyHit, looting, 0.0F);
    }

    @Override
    public boolean interact(EntityPlayer player) {
        if (!this.canInteract(player))
            return super.interact(player);
        ItemStack playerHeld = player.getEquipmentInSlot(0);
        if (playerHeld == null && this.getEquipmentInSlot(0) == null)
            return super.interact(player);
        if (playerHeld == null) {
            this.setEquipment(0, null);
        }
        else if (player.isSneaking())
            return super.interact(player);
        else {
            ItemStack heldItem = this.getEquipmentInSlot(0);
            if (!this.worldObj.isRemote) {
                ItemStack split = playerHeld.copy();
                split.stackSize = 1;
                this.setEquipment(0, split);
            }
            if (!player.capabilities.isCreativeMode) {
                playerHeld.stackSize--;
            }
            if (playerHeld.stackSize <= 0) {
                player.setCurrentItemOrArmor(0, null);
            }
        }
        return true;
    }

    public boolean canInteract(EntityPlayer player) {
        return this.isEntityAlive() && (player.getCommandSenderName() == this.getCreator());
    }

    public boolean setEquipment(ItemStack itemStack) {
        if (itemStack != null) {
            for (int slot = 0; slot < 5; slot++) {
                if (this.getEquipmentInSlot(slot) != null)
                    return this.setEquipment(slot, itemStack);
            }
            return false;
        }
        int slot = 0;
        if (itemStack.getItem() instanceof ItemArmor) {
            slot = 4 - ((ItemArmor)itemStack.getItem()).armorType;
        }
        return this.setEquipment(slot, itemStack);
    }

    public boolean setEquipment(int slot, ItemStack itemStack) {
        if (!this.worldObj.isRemote && this.getEquipmentInSlot(slot) != null) {
            this.entityDropItem(this.getEquipmentInSlot(slot), 0.0F);
        }
        this.setCurrentItemOrArmor(slot, itemStack);
        this.equipmentDropChances[slot] = 2.0F;
        return true;
    }

    /// Executes this scarecrow's ranged attack.
    public void doRangedAttack(EntityLivingBase target) {
        ItemStack held = this.getEquipmentInSlot(0);
        if (held == null)
            return;
        else if (held.getItem() instanceof ItemBow) {
            EntityArrow arrow = new EntityArrow(this.worldObj, this, target, 1.6F, 12.0F);
            this.playSound("random.bow", 1.0F, 1.0F / (this.rand.nextFloat() * 0.4F + 0.8F));
            int power = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, held);
            int punch = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, held);
            if (power > 0) {
                arrow.setDamage(arrow.getDamage() + power * 0.5 + 0.5);
            }
            if (punch > 0) {
                arrow.setKnockbackStrength(punch);
            }
            if (EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, held) > 0) {
                arrow.setFire(100);
            }
            this.worldObj.spawnEntityInWorld(arrow);
        }
        else {
            EntitySnowball snowball = new EntitySnowball(this.worldObj, this);
            double motionX = target.posX - this.posX;
            double motionY = target.posY + target.getEyeHeight() - 1.1 - snowball.posY;
            double motionZ = target.posZ - this.posZ;
            float velocity = (float)Math.sqrt(motionX * motionX + motionZ * motionZ) * 0.2F;
            snowball.setThrowableHeading(motionX, motionY + velocity, motionZ, 1.6F, 12.0F);
            this.playSound("random.bow", 1.0F, 1.0F / (this.rand.nextFloat() * 0.4F + 0.8F));
            this.worldObj.spawnEntityInWorld(snowball);
        }
    }

    /// Called when the entity is attacked.
    @Override
    public boolean attackEntityFrom(DamageSource damageSource, float damage) {
        if (this.isEntityInvulnerable())
            return false;
        return super.attackEntityFrom(damageSource, damage);
    }

    @Override
    public IEntityLivingData onSpawnWithEgg(IEntityLivingData data) {
        data = super.onSpawnWithEgg(data);
        this.setCurrentItemOrArmor(4, new ItemStack(Blocks.pumpkin));
        this.equipmentDropChances[4] = 0.0F;
        return data;
    }

    @Override
    protected boolean canTriggerWalking() {
        return false;
    }

    @Override
    protected Item getDropItem() {
        return Item.getItemFromBlock(Blocks.wool);
    }

    protected void dropFewItems(boolean recentlyHit, int looting, float dropChance) {
        for (int i = this.rand.nextInt(3); i-- > 0;) {
            this.dropItem(Item.getItemFromBlock(Blocks.fence), 1);
        }
        if (this.rand.nextInt(2) == 0) {
            this.dropItem(this.getDropItem(), 1);
        }
    }

    @Override
    public boolean isEntityApplicable(Entity entity) {
        if (entity instanceof EntityPlayer) {
            if (entity.getCommandSenderName() == this.getCreator()) {
                return false;
            }
        }

        return (entity instanceof IMob);
    }
}