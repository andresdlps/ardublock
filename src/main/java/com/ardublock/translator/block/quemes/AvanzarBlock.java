package com.ardublock.translator.block.quemes;

import com.ardublock.translator.Translator;
import com.ardublock.translator.block.TranslatorBlock;
import com.ardublock.translator.block.exception.BlockException;
import com.ardublock.translator.block.exception.SocketNullException;
import com.ardublock.translator.block.exception.SubroutineNotDeclaredException;

/**
 *
 * @author andres
 */
public class AvanzarBlock extends TranslatorBlock{

    public AvanzarBlock(Long blockId, Translator translator, String codePrefix, String codeSuffix, String label) {
        super(blockId, translator);
    }
    
    
    @Override
    public String toCode() throws SocketNullException, SubroutineNotDeclaredException, BlockException {
        String left = this.getRequiredTranslatorBlockAtSocket(0).toCode().replaceAll("\"", "");
        String pinleft = this.getRequiredTranslatorBlockAtSocket(1).toCode().replaceAll("\"", "");
        String right = this.getRequiredTranslatorBlockAtSocket(2).toCode().replaceAll("\"", "");
        String pinright = this.getRequiredTranslatorBlockAtSocket(3).toCode().replaceAll("\"", "");
        String frontSensors = this.getRequiredTranslatorBlockAtSocket(4).toCode().replaceAll("\"", "");
        translator.addDefinitionCommand("long lastError;");
        translator.addDefinitionCommand("long sumError;");
        translator.addDefinitionCommand("void forward(){ \n" +
                                        "  int velRight = STOP_"+ right +" - 10;\n" +
                                        "  int velLeft = STOP_"+ left +" + 15;\n" +
                                        "  int position = "+frontSensors+".readLine("+ frontSensors + "_values);  \n" +
                                        "  long error = (position-500);\n" +
                                        "  error = (error * 90);\n" +
                                        "  error = error / 500;\n" +
                                        "\n" +
                                        "  sumError = sumError + error;\n" +
                                        "  long difError = error - lastError;\n" +
                                        "  float P = 0.1;\n" +
                                        "  float I = 0.001;\n" +
                                        "  float D = 0.05;\n" +
                                        "  int v = (P*error) + (D * difError) + (I * sumError);\n" +
                                        "  if(abs(error)<10){\n" +
                                        "    v=0;\n" +
                                        "  }\n" +
                                        "  lastError = error;\n" +
                                        "  \n" +
                                        "  velLeft = velLeft + v; \n" +
                                        "  velRight = velRight + v;\n" +
                                        "  servo_pin_"+pinleft+".write(velLeft);\n" +
                                        "  servo_pin_"+pinright+".write(velRight);  \n" +
                                        "}");
        TranslatorBlock t = this.getRequiredTranslatorBlockAtSocket(5);
        return   "    lastError = 0;\n" +
                "    sumError = 0;\n"
                + "do { \n" +
            "          forward();\n" +
            "    } while("+ t.toCode() +") ; \n" +
            "    do { \n" +
            "          forward();\n" +
            "    } while(!"+ t.toCode() +");\n" +
                "  servo_pin_"+pinleft+".write(STOP_"+ left +");\n" +
                "  servo_pin_"+pinright+".write(STOP_"+ right +");  \n";
    }
    
        
}
