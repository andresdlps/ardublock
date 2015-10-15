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
public class SensorInLineBlock extends TranslatorBlock {

    public SensorInLineBlock(Long blockId, Translator translator, String codePrefix, String codeSuffix, String label) {
        super(blockId, translator, codePrefix, codeSuffix, label);
    }    
    
    @Override
    public String toCode() throws SocketNullException, SubroutineNotDeclaredException, BlockException {
        StringBuffer function = new StringBuffer();
        String name = this.getRequiredTranslatorBlockAtSocket(0).toCode().replaceAll("\"", "");
        function.append("boolean "+name+"InLine(){\n");
        function.append(name).append(".read(").append(name).append("_values);\n");
        function.append("long sum = 0;\n" +
                "  for (unsigned char i = 0; i < " + name +"_NUM_SENSORS; i++)\n" +
                "  {\n" +
                "    sum = sum +  "+name+"_values[i];\n" +
                "  }");
        function.append("long maxvalue = " + name +"_NUM_SENSORS * 2500;");
        function.append("long threshold = " + name +"_NUM_SENSORS * 2500 * 0.65;");
        function.append("if(sum > (maxvalue - threshold)){\n" +
                    "    return true;\n" +
                    "  }else{\n" +
                    "    return false;\n" +
                    "  }");
        function.append("}\n");
        translator.addDefinitionCommand(function.toString());
        StringBuffer sb = new StringBuffer();
        sb.append(name).append("InLine()");
        return this.codePrefix + sb.toString() + this.codeSuffix;
    }
    
}
