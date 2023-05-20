/**
 *  Thaumic Augmentation
 *  Copyright (c) 2019 TheCodex6824.
 *
 *  This file is part of Thaumic Augmentation.
 *
 *  Thaumic Augmentation is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Thaumic Augmentation is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with Thaumic Augmentation.  If not, see <https://www.gnu.org/licenses/>.
 */

package cn.fhyjs.jntm.renderer;

import cn.fhyjs.jntm.interfaces.IAnimatedTile;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.model.animation.AnimationTESR;
import net.minecraftforge.common.animation.Event;

public class ListeningAnimatedTESR<TE extends TileEntity & IAnimatedTile> extends AnimationTESR<TE>{

    @Override
    public void handleEvents(TE te, float time, Iterable<Event> pastEvents) {
        te.handleEvents(time, pastEvents);
    }

}
