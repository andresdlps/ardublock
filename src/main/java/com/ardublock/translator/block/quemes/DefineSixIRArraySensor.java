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
public class DefineSixIRArraySensor extends TranslatorBlock  {

    public DefineSixIRArraySensor(Long blockId, Translator translator, String codePrefix, String codeSuffix, String label) {
        super(blockId, translator, codePrefix, codeSuffix, label);
    }

    @Override
    public String toCode() throws SocketNullException, SubroutineNotDeclaredException, BlockException {
        translator.addHeaderFile("QTRSensors.h");
        
        String name = this.getTranslatorBlockAtSocket(0).toCode().replaceAll("\"", "");
        
        translator.addDefinitionCommand("#define "+ name + "_NUM_SENSORS 6");
        translator.addDefinitionCommand("#define TIMEOUT 2500 ");
        translator.addDefinitionCommand("#define EMITTER_PIN "+ this.getTranslatorBlockAtSocket(1).toCode());
        
        translator.addDefinitionCommand("QTRSensorsRC "+ name + "((unsigned char[]) {"
                                        + this.getTranslatorBlockAtSocket(2).toCode() +
                                        ", "+ this.getTranslatorBlockAtSocket(3).toCode() + 
                                        ", "+ this.getTranslatorBlockAtSocket(4).toCode() + 
                                        ", "+ this.getTranslatorBlockAtSocket(5).toCode() + 
                                        ", "+ this.getTranslatorBlockAtSocket(6).toCode() + 
                                        ", "+ this.getTranslatorBlockAtSocket(7).toCode() + "}, "+ name + "_NUM_SENSORS, TIMEOUT, EMITTER_PIN); ");
        
        translator.addDefinitionCommand("unsigned int "+ name + "_values["+ name + "_NUM_SENSORS];");
        return "";
    }
    
}
