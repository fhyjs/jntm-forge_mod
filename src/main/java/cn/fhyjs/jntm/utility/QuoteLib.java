package cn.fhyjs.jntm.utility;

import net.katsstuff.teamnightclipse.danmakucore.danmaku.form.Form;
import net.katsstuff.teamnightclipse.danmakucore.lib.data.LibForms;
import net.minecraft.item.Item;
import net.minecraftforge.common.util.EnumHelper;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class QuoteLib {
    public static List<Form> FormArray = getAllForm();
    public static Item.ToolMaterial UseLessMaterial = EnumHelper.addToolMaterial("uselessmaterial",0,1,1,0,1);

    public static List<Form> getAllForm() {
        List<Form> FormArray = new ArrayList<>();
        Class CLibForm;
        try {
            CLibForm = Class.forName(LibForms.class.getName());
            Field[] fields = CLibForm.getFields();
            for (Field field:fields){
                field.setAccessible(true);
                try {
                    Object vaule = field.get(null);
                    if (vaule instanceof Form){
                        FormArray.add((Form) vaule);
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return FormArray;
    }

}
