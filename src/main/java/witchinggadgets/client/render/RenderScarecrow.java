package witchinggadgets.client.render;

import witchinggadgets.client.model.ModelScarecrow;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelSkeleton;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import witchinggadgets.common.mob.EntityScarecrow;

@SideOnly(Side.CLIENT)
public class RenderScarecrow extends RenderBiped
{
    public RenderScarecrow() {
        this(new ModelScarecrow());
    }

    public RenderScarecrow(ModelBiped model) {
        super(model, 0.5F, 1.0F);
    }

    /// Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return ((EntityScarecrow)entity).getTexture();
    }

    /// Used to initialize armor models.
    @Override
    protected void func_82421_b() {
        this.field_82423_g = new ModelBiped(1.0F);
        this.field_82425_h = new ModelBiped(0.5F);
    }

    /// Translates the model.
    @Override
    protected void func_82422_c() {
        GL11.glTranslatef(this.modelBipedMain instanceof ModelSkeleton ? 0.09375F : 0.0F, 0.1875F, 0.0F);
    }
}