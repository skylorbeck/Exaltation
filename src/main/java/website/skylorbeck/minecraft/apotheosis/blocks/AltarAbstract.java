package website.skylorbeck.minecraft.apotheosis.blocks;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import website.skylorbeck.minecraft.apotheosis.blocks.entities.AltarEntity;
import website.skylorbeck.minecraft.apotheosis.blocks.screens.AltarScreenHandler;

import java.util.Random;

public class AltarAbstract extends BlockWithEntity {
    public static final DirectionProperty FACING;
    private int tier = 0;

    public AltarAbstract(Settings settings, int tier) {
        super(settings);
        this.tier = tier;
        this.setDefaultState(this.getDefaultState().with(FACING, Direction.NORTH));

    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        VoxelShape base = Block.createCuboidShape(1D, 0.0D, 1D, 15D, 4D, 15D);
        VoxelShape base2 = Block.createCuboidShape(6D, 4D, 6D,10D, 14D, 10D);
        VoxelShape base3 =  Block.createCuboidShape(2D, 14D, 2D, 14D, 17D, 14D);
        return VoxelShapes.union(base,base2,base3);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.INVISIBLE;
    }

    @Override
    public BlockState getPlacementState(@NotNull ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getPlayerFacing().getOpposite());
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new AltarEntity(pos, state, tier);
    }

    @Nullable
    @Override
    public NamedScreenHandlerFactory createScreenHandlerFactory(BlockState state, World world, BlockPos pos) {
        return new ExtendedScreenHandlerFactory() {
            @Override
            public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
                buf.writeBlockPos(pos);
            }

            @Override
            public Text getDisplayName() {
                return new TranslatableText("apotheosis.altar.name");
            }

            @Nullable
            @Override
            public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
                return new AltarScreenHandler(syncId, inv, ScreenHandlerContext.create(world, pos), pos);
            }
        };
    }

    @Override
    public ActionResult onUse(BlockState state, @NotNull World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient) {
            return ActionResult.SUCCESS;
        } else {
            player.openHandledScreen(state.createScreenHandlerFactory(world, pos));
//            player.incrementStat(Stats.);//todo
            return ActionResult.CONSUME;
        }

    }

    @Override
    public boolean hasRandomTicks(BlockState state) {
        return true;
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (random.nextInt(10) == 0) {
            world.playSound((double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D, SoundEvents.BLOCK_CAMPFIRE_CRACKLE, SoundCategory.BLOCKS, 0.5F + random.nextFloat(), random.nextFloat() * 0.7F + 0.6F, false);
        }

        if (random.nextInt(10) == 0) {
            for(int i = 0; i < random.nextInt(1) + 1; ++i) {
                DefaultParticleType flame = ParticleTypes.FLAME;
                DefaultParticleType smoke = ParticleTypes.SMOKE;
                if (tier >= 3) {
                    flame=ParticleTypes.SOUL_FIRE_FLAME;
                }
                world.addParticle(flame, (double) pos.getX() + 0.5D, (double) pos.getY() + 1.1D, (double) pos.getZ() + 0.5D, random.nextFloat() / 50, random.nextFloat() / 20, random.nextFloat() / 50);
                world.addParticle(smoke, (double) pos.getX() + 0.5D, (double) pos.getY() + 1.1D, (double) pos.getZ() + 0.5D, random.nextFloat() / 50, 0.0D, random.nextFloat() / 50);
            }
        }
        if (random.nextInt(100)<=this.tier){
            world.addParticle(ParticleTypes.SOUL, (double) pos.getX() + 0.5D, (double) pos.getY() + 1.1D, (double) pos.getZ() + 0.5D, 0, 0.1D, 0);

        }

        super.randomDisplayTick(state, world, pos, random);
    }

    public int getTier() {
        return tier;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
        stateManager.add(Properties.HORIZONTAL_FACING);
    }

    static {
        FACING = HorizontalFacingBlock.FACING;
    }
}