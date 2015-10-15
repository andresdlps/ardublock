/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ardublock.translator.block.quemes;

import com.ardublock.translator.Translator;
import com.ardublock.translator.block.TranslatorBlock;
import com.ardublock.translator.block.exception.BlockException;
import com.ardublock.translator.block.exception.SocketNullException;
import com.ardublock.translator.block.exception.SubroutineNotDeclaredException;

/**
 *
 * @author z420
 */
public class TurnBlock extends TranslatorBlock {

    public TurnBlock(Long blockId, Translator translator, String codePrefix, String codeSuffix, String label) {
        super(blockId, translator, codePrefix, codeSuffix, label);
    }

    @Override
    public String toCode() throws SocketNullException, SubroutineNotDeclaredException, BlockException {
        String dir = this.getRequiredTranslatorBlockAtSocket(0).toCode().replaceAll("\"", "");
        String left = this.getRequiredTranslatorBlockAtSocket(1).toCode().replaceAll("\"", "");
        String pinleft = this.getRequiredTranslatorBlockAtSocket(2).toCode().replaceAll("\"", "");
        String right = this.getRequiredTranslatorBlockAtSocket(3).toCode().replaceAll("\"", "");
        String pinright = this.getRequiredTranslatorBlockAtSocket(4).toCode().replaceAll("\"", "");
        translator.addDefinitionCommand("void turn(boolean left){\n" +
                                        "  int velRight = STOP_"+ right +";\n" +
                                        "  int velLeft = STOP_"+ left +";\n" +
                                        "  if (left){\n" +
                                        "    velRight+=10;\n" +
                                        "    velLeft+=10;\n" +
                                        "  }else{\n" +
                                        "    velRight-=8;\n" +
                                        "    velLeft-=8;\n" +
                                        "  }\n" +
                                        "  servo_pin_"+pinleft+".write(velLeft);\n" +
                                        "  servo_pin_"+pinright+".write(velRight);\n" +
                                        "}");
         TranslatorBlock t = this.getRequiredTranslatorBlockAtSocket(5);
        return "do { \n" +
"          turn("+dir+");\n" +
"        }  \n" +
"        while("+ t.toCode() +"); \n" +
"        do { \n" +
"          turn("+dir+");\n" +
"        } while(!"+ t.toCode() +");\n"+
    "  servo_pin_"+pinleft+".write(STOP_"+ left +");\n" +
   "  servo_pin_"+pinright+".write(STOP_"+ right +");  \n";
    }
    
}
