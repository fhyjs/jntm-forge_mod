package cn.fhyjs.jntm.client;

import cn.fhyjs.jntm.common.Mcefcmp;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResourceManager;
import net.minecraftforge.client.resource.IResourceType;
import net.minecraftforge.client.resource.ISelectiveResourceReloadListener;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.montoyo.mcef.api.*;
import net.montoyo.mcef.example.ScreenCfg;
import net.montoyo.mcef.utilities.Log;

import java.util.function.Predicate;
@Optional.InterfaceList(value={
        @Optional.Interface(iface="net.montoyo.mcef.api.IDisplayHandler", modid="mcef", striprefs=true)
})
public class Mcefclp extends Mcefcmp implements ISelectiveResourceReloadListener, IDisplayHandler, IJSQueryHandler {
    private static net.montoyo.mcef.api.API mcef;

    public ScreenCfg hudBrowser = null;
    @Override
    public void setBackup(Object bu) {
        this.backup = bu;
    }
    @Override
    public boolean hasBackup() {
        return this.backup != null;
    }
    private Object backup = null;
    public net.montoyo.mcef.api.API getAPI() {
        return mcef;
    }
    @Override
    public void preInit(FMLPreInitializationEvent event){
        super.preInit(event);
        //---browser---
        Minecraft mc = Minecraft.getMinecraft();
        MinecraftForge.EVENT_BUS.register(this);

        mcef = MCEFApi.getAPI();
        if(mcef != null)
            mcef.registerScheme("jntm", WDScheme.class, true, false, false, true, true, false, false);
        //---browser---
    }
    @Override
    public void postInit(FMLPostInitializationEvent event){
        super.postInit(event);
        //---browser---
        if(mcef == null)
            throw new RuntimeException("MCEF is missing");

        mcef.registerDisplayHandler(this);
        mcef.registerJSQueryHandler(this);
        //---browser---
    }
    //---browser---
    @Override
    public void onResourceManagerReload(IResourceManager resourceManager, Predicate<IResourceType> resourcePredicate) {
        Log.info("Resource manager reload: clearing GUI cache...");
    }

    @Override
    public void onAddressChange(IBrowser browser, String url) {
        if(browser != null) {

        }
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
        if(browser != null && persistent && query != null && cb != null) {
            query = query.toLowerCase();

            if(query.startsWith("webdisplays_")) {
                query = query.substring(12);

                String args;
                int parenthesis = query.indexOf('(');
                if(parenthesis < 0)
                    args = null;
                else {
                    if(query.indexOf(')') != query.length() - 1) {
                        cb.failure(400, "Malformed request");
                        return true;
                    }

                    args = query.substring(parenthesis + 1, query.length() - 1);
                    query = query.substring(0, parenthesis);
                }
/*
                if(jsDispatcher.canHandleQuery(query))
                    jsDispatcher.enqueueQuery(browser, query, args, cb);
                else
                    cb.failure(404, "Unknown WebDisplays query");
*/
                return true;
            }
        }

        return false;
    }

    @Override
    public void cancelQuery(IBrowser iBrowser, long l) {

    }
    //---browser---
}
