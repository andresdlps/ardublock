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
public class PararBlock extends TranslatorBlock{

    public PararBlock(Long blockId, Translator translator, String codePrefix, String codeSuffix, String label) {
        super(blockId, translator, codePrefix, codeSuffix, label);
    }

    @Override
    public String toCode() throws SocketNullException, SubroutineNotDeclaredException, BlockException {
        String left = this.getRequiredTranslatorBlockAtSocket(0).toCode().replaceAll("\"", "");
        String pinleft = this.getRequiredTranslatorBlockAtSocket(1).toCode().replaceAll("\"", "");
        String right = this.getRequiredTranslatorBlockAtSocket(2).toCode().replaceAll("\"", "");
        String pinright = this.getRequiredTranslatorBlockAtSocket(3).toCode().replaceAll("\"", "");
        return "  servo_pin_"+pinleft+".write(STOP_"+ left +");\n" +
               "  servo_pin_"+pinright+".write(STOP_"+ right +");  \n";
    }
    
}
