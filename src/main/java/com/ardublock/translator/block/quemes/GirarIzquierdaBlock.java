
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
public class GirarIzquierdaBlock extends TranslatorBlock {

    public GirarIzquierdaBlock(Long blockId, Translator translator, String codePrefix, String codeSuffix, String label) {
        super(blockId, translator, codePrefix, codeSuffix, label);
    }

    @Override
    public String toCode() throws SocketNullException, SubroutineNotDeclaredException, BlockException {
        String s = "    do { \n" +
                "    giraIzquierda();\n" +
                "  } while(!enCruceG());\n" +
                "  do { \n" +
                "    giraIzquierda();\n" +
                "  }  while(enCruceG()); \n" +
                "  do { \n" +
                "    giraIzquierda();\n" +
                "  } while(!enCruceG());\n";
        return s;
    }
    
}
