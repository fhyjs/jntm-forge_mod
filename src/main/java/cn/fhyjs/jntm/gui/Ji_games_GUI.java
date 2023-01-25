package cn.fhyjs.jntm.gui;

import cn.fhyjs.jntm.Jntm;
import cn.fhyjs.jntm.client.Mcefclp;
import cn.fhyjs.jntm.common.CommonProxy;
import cn.fhyjs.jntm.config.ConfigHandler;
import cn.fhyjs.jntm.network.JntmMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.toasts.SystemToast;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

import net.minecraftforge.fml.common.Optional;
import net.montoyo.mcef.MCEF;
import net.montoyo.mcef.api.API;
import net.montoyo.mcef.api.IBrowser;
import net.montoyo.mcef.api.MCEFApi;
import net.montoyo.mcef.example.ScreenCfg;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

@Optional.InterfaceList(value={
        @Optional.Interface(iface="net.montoyo.mcef.api.IDisplayHandler", modid="mcef", striprefs=true)
})
public class Ji_games_GUI extends GuiScreen {
    Mcefclp Mcefcmprofy;
    IBrowser browser = null;
    private GuiButton back = null;
    private GuiButton fwd = null;
    private GuiButton go = null;
    private GuiButton min = null;
    private GuiButton vidMode = null;
    private GuiTextField url = null;
    private String urlToLoad = null;


    public Ji_games_GUI(EntityPlayer player, World world) {
        this.urlToLoad = MCEF.HOME_PAGE;
    }


    public void initGui() {
        Mcefcmprofy.hudBrowser = null;
        if (this.browser == null) {
            API api = MCEFApi.getAPI();
            if (api == null) {
                return;
            }

            this.browser = api.createBrowser("mod://jntm/index.html", false);
            this.urlToLoad = null;
        }

        if (this.browser != null) {
            this.browser.resize(this.mc.displayWidth, this.mc.displayHeight - this.scaleY(20));
        }

        Keyboard.enableRepeatEvents(true);
        this.buttonList.clear();
        if (this.url == null) {
            this.buttonList.add(this.back = new GuiButton(0, 0, 0, 20, 20, "<"));
            this.buttonList.add(this.fwd = new GuiButton(1, 20, 0, 20, 20, ">"));
            this.buttonList.add(this.go = new GuiButton(2, this.width - 60, 0, 20, 20, "Go"));
            this.buttonList.add(this.min = new GuiButton(3, this.width - 20, 0, 20, 20, "X"));
            this.vidMode = new GuiButton(4, this.width - 40, 0, 20, 20, "刷新");
            this.buttonList.add(vidMode);
            this.url = new GuiTextField(5, this.fontRenderer, 40, 0, this.width - 100, 20);
            this.url.setMaxStringLength(65535);
        } else {
            this.buttonList.add(this.back);
            this.buttonList.add(this.fwd);
            this.buttonList.add(this.go);
            this.buttonList.add(this.min);
            this.buttonList.add(this.vidMode);
            this.vidMode.x = this.width - 40;
            this.go.x = this.width - 60;
            this.min.x = this.width - 20;
            String old = this.url.getText();
            this.url = new GuiTextField(5, this.fontRenderer, 40, 0, this.width - 100, 20);
            this.url.setMaxStringLength(65535);
            this.url.setText(old);
        }

    }

    public int scaleY(int y) {
        double sy = (double)y / (double)this.height * (double)this.mc.displayHeight;
        return (int)sy;
    }

    public void loadURL(String url) {
        if (this.browser == null) {
            this.urlToLoad = url;
        } else {
            this.browser.loadURL(url);
        }

    }

    public void updateScreen() {
        if (this.urlToLoad != null && this.browser != null) {
            this.browser.loadURL(this.urlToLoad);
            this.urlToLoad = null;
        }

    }

    public void drawScreen(int i1, int i2, float f) {
        this.url.drawTextBox();
        super.drawScreen(i1, i2, f);
        if (this.browser != null) {
            GlStateManager.disableDepth();
            GlStateManager.enableTexture2D();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.browser.draw(0.0, (double)this.height, (double)this.width, 20.0);
            GlStateManager.enableDepth();
        }

    }


    public void onGuiClosed() {
        if (this.browser != null) {
            this.browser.close();
        }

        Keyboard.enableRepeatEvents(false);
    }

    public void handleInput() {
        boolean pressed;
        int key;
        int num;
        while(Keyboard.next()) {
            if (Keyboard.getEventKey() == 1) {
                this.mc.displayGuiScreen((GuiScreen)null);
                return;
            }

            pressed = Keyboard.getEventKeyState();
            pressed = this.url.isFocused();
            key = Keyboard.getEventCharacter();
            num = Keyboard.getEventKey();
            if (this.browser != null && !pressed) {
                if (pressed) {
                    this.browser.injectKeyPressedByKeyCode(num, (char)key, 0);
                } else {
                    this.browser.injectKeyReleasedByKeyCode(num, (char)key, 0);
                }

                if (key != 0) {
                    this.browser.injectKeyTyped((char)key, 0);
                }
            }

            if (!pressed && pressed && num == 28) {
                this.actionPerformed(this.go);
            } else if (pressed) {
                this.url.textboxKeyTyped((char)key, num);
            }
        }

        while(Mouse.next()) {
            int btn = Mouse.getEventButton();
            pressed = Mouse.getEventButtonState();
            key = Mouse.getEventX();
            num = Mouse.getEventY();
            int wheel = Mouse.getEventDWheel();
            int x;
            if (this.browser != null) {
                x = this.mc.displayHeight - num - this.scaleY(20);
                if (wheel != 0) {
                    this.browser.injectMouseWheel(key, x, 0, 1, wheel);
                } else if (btn == -1) {
                    this.browser.injectMouseMove(key, x, 0, x < 0);
                } else {
                    this.browser.injectMouseButton(key, x, 0, btn + 1, pressed, 1);
                }
            }

            if (pressed) {
                x = key * this.width / this.mc.displayWidth;
                int y = this.height - num * this.height / this.mc.displayHeight - 1;

                try {
                    this.mouseClicked(x, y, btn);
                } catch (Throwable var9) {
                    var9.printStackTrace();
                }

                this.url.mouseClicked(x, y, btn);
            }
        }

    }

    public void onUrlChanged(IBrowser b, String nurl) {
        if (b == this.browser && this.url != null) {
            this.url.setText(nurl);
            this.vidMode.enabled = nurl.matches("^https?://(?:www\\.)?youtube\\.com/watch\\?v=([a-zA-Z0-9_\\-]+)$") || nurl.matches("^https?://(?:www\\.)?youtu\\.be/([a-zA-Z0-9_\\-]+)$") || nurl.matches("^https?://(?:www\\.)?youtube\\.com/embed/([a-zA-Z0-9_\\-]+)(\\?.+)?$");
        }

    }

    protected void actionPerformed(GuiButton src) {
        if (this.browser != null) {
            if (src.id == 0) {
                this.browser.goBack();
            } else if (src.id == 1) {
                this.browser.goForward();
            } else {
                String loc;
                if (src.id == 2) {
                    loc = Mcefcmprofy.getAPI().punycode(this.url.getText());
                    this.browser.loadURL(loc);
                } else if (src.id == 3) {
                    Mcefcmprofy.setBackup(this);
                    this.mc.displayGuiScreen((GuiScreen)null);
                } else if (src.id == 4) {
                    browser.loadURL(browser.getURL());
                }
            }

        }
    }
}
