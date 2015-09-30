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
        String right = this.getRequiredTranslatorBlockAtSocket(1).toCode().replaceAll("\"", "");
        String frontSensors = this.getRequiredTranslatorBlockAtSocket(2).toCode().replaceAll("\"", "");
        translator.addDefinitionCommand("int lastError;");
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
                                        "  servo_"+left+".write(velLeft);\n" +
                                        "  servo_"+right+".write(velRight);  \n" +
                                        "}");
        TranslatorBlock t = this.getRequiredTranslatorBlockAtSocket(3);
        return   "do { \n" +
            "          forward();\n" +
            "    } while("+ t.toCode() +") ; \n" +
            "    do { \n" +
            "          forward();\n" +
            "    } while(!"+ t.toCode() +");\n";
    }
    
        
}
