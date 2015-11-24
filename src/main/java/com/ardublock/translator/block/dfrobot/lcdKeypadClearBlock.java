/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ardublock.translator.block.dfrobot;

import com.ardublock.translator.Translator;
import com.ardublock.translator.block.TranslatorBlock;
import com.ardublock.translator.block.exception.BlockException;
import com.ardublock.translator.block.exception.SocketNullException;
import com.ardublock.translator.block.exception.SubroutineNotDeclaredException;

/**
 *
 * @author z420
 */
public class lcdKeypadClearBlock extends TranslatorBlock {

    public lcdKeypadClearBlock(Long blockId, Translator translator, String codePrefix, String codeSuffix, String label) {
        super(blockId, translator, codePrefix, codeSuffix, label);
    }

    @Override
    public String toCode() throws SocketNullException, SubroutineNotDeclaredException, BlockException {
        translator.addHeaderFile("LiquidCrystal.h");
        translator.addDefinitionCommand("LiquidCrystal lcd(8, 9, 4, 5, 6, 7);");
        translator.addSetupCommand("lcd.begin(16, 2);");
        String ret = "lcd.clear();";
        return ret;
    }
    
}
