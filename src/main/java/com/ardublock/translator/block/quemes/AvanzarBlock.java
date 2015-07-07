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
        return "do { \n" +
            "          sigueLinea();\n" +
            "    } while(sobreCruce()) ; \n" +
            "    do { \n" +
            "          sigueLinea();\n" +
            "    } while(!sobreCruce());\n";
    }
    
        
}
