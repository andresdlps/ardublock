/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ardublock.translator.block.quemes;

import com.ardublock.translator.Translator;
import com.ardublock.translator.block.NumberBlock;
import com.ardublock.translator.block.TranslatorBlock;
import com.ardublock.translator.block.exception.BlockException;
import com.ardublock.translator.block.exception.SocketNullException;
import com.ardublock.translator.block.exception.SubroutineNotDeclaredException;

/**
 *
 * @author z420
 */
public class CalibrateMotor extends TranslatorBlock {

    public CalibrateMotor(Long blockId, Translator translator, String codePrefix, String codeSuffix, String label) {
        super(blockId, translator, codePrefix, codeSuffix, label);
    }

    @Override
    public String toCode() throws SocketNullException, SubroutineNotDeclaredException, BlockException {
        String name = this.getRequiredTranslatorBlockAtSocket(0).toCode().replaceAll("\"", "");
        String angle = this.getRequiredTranslatorBlockAtSocket(2).toCode();
        translator.addDefinitionCommand("#define STOP_" + name + " " + angle);

        TranslatorBlock translatorBlock = this.getRequiredTranslatorBlockAtSocket(1);
        if (!(translatorBlock instanceof NumberBlock)) {
            throw new BlockException(this.blockId, "the Pin# of Servo must a number");
        }
        String pinNumber = translatorBlock.toCode();
        String servoName = "servo_" + name;

        String ret = servoName + ".write( " + angle + " );\n";
        translator.addHeaderFile("Servo.h");
        translator.addDefinitionCommand("Servo " + servoName + ";");
        translator.addSetupCommand(servoName + ".attach(" + pinNumber + ");");
        return ret;
    }

}
