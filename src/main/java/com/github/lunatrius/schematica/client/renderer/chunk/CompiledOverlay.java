package com.github.lunatrius.schematica.client.renderer.chunk;

import net.minecraft.client.renderer.chunk.CompiledChunk;
import net.minecraft.util.BlockRenderLayer;

public class CompiledOverlay extends CompiledChunk {
	
	BlockRenderLayer blockType = BlockRenderLayer.TRANSLUCENT;
//	BlockRenderLayer blockType = BlockRenderLayer.CUTOUT_MIPPED;
	
    @Override
    public void setLayerStarted(final BlockRenderLayer layer) {
        if (layer == blockType) {
            super.setLayerStarted(layer);
        }
    }

    @Override
    public void setLayerUsed(final BlockRenderLayer layer) {
        if (layer == blockType) {
            super.setLayerUsed(layer);
        }
    }

    @Override
    public boolean isLayerStarted(final BlockRenderLayer layer) {
        return layer == blockType && super.isLayerStarted(layer);
    }

    @Override
    public boolean isLayerEmpty(final BlockRenderLayer layer) {
        return layer == blockType && super.isLayerEmpty(layer);
    }
}
