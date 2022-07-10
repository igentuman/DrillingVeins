package igentuman.dveins.command;

import igentuman.dveins.RegistryHandler;
import net.minecraft.block.Block;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;


public class CommandStripChunk extends CommandBase {
	
	@Override
	public String getName() {
		return "strip_chunk";
	}
	
	@Override
	public String getUsage(ICommandSender sender) {
		return "";
	}
	
	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}
	
	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
		Chunk chunk = sender.getEntityWorld().getChunk(sender.getPosition());
		if (chunk != null && chunk.isLoaded()) {
			for(int x = 0; x < 16; x++) {
				for(int z = 0; z < 16; z++) {
					for(int y = 1; y < 254; y++) {
						BlockPos p = new BlockPos(chunk.x*16+x, y, chunk.z*16+z);
						Block b = chunk.getBlockState(p).getBlock();
						if(b.equals(Blocks.AIR) || RegistryHandler.oreBlocks.contains(b)) {
							continue;
						}
						chunk.getWorld().setBlockToAir(p);
					}
				}
			}
		}
	}
}
