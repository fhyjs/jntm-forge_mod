package cn.fhyjs.jntm.common;

import cn.fhyjs.jntm.client.WDScheme;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.client.resource.IResourceType;
import net.minecraftforge.client.resource.ISelectiveResourceReloadListener;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.montoyo.mcef.api.*;
import net.montoyo.mcef.example.ScreenCfg;
import net.montoyo.mcef.utilities.Log;
import org.lwjgl.opengl.Display;

import java.util.function.Predicate;
@Optional.InterfaceList(value={
        @Optional.Interface(iface="net.montoyo.mcef.api.IDisplayHandler", modid="mcef", striprefs=true)
})
public class Mcefcmp  implements ISelectiveResourceReloadListener, IDisplayHandler, IJSQueryHandler {
    private net.montoyo.mcef.api.API mcef;

    public ScreenCfg hudBrowser = null;

    public void setBackup(Object bu) {
        this.backup = bu;
    }

    public boolean hasBackup() {
        return this.backup != null;
    }
    private Object backup = null;
    public net.montoyo.mcef.api.API getAPI() {
        return mcef;
    }

    public void preInit(FMLPreInitializationEvent event){

    }

    public void postInit(FMLPostInitializationEvent event) {
    }
    @Override
    public void onResourceManagerReload(IResourceManager resourceManager, Predicate<IResourceType> resourcePredicate) {
    }

    @Override
    public void onAddressChange(IBrowser browser, String url) {
    }

    @Override
    public void onTitleChange(IBrowser iBrowser, String s) {

    }

    @Override
    public void onTooltip(IBrowser iBrowser, String s) {

    }

    @Override
    public void onStatusMessage(IBrowser iBrowser, String s) {

    }

    @Override
    public boolean handleQuery(IBrowser browser, long queryId, String query, boolean persistent, IJSQueryCallback cb) {
        return persistent;
    }

    @Override
    public void cancelQuery(IBrowser iBrowser, long l) {

    }
    //---browser---
}
