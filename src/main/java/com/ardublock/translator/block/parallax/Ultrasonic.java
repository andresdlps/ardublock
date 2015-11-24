/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ardublock.translator.block.parallax;


import com.ardublock.translator.Translator;
import com.ardublock.translator.block.TranslatorBlock;
import com.ardublock.translator.block.exception.SocketNullException;
import com.ardublock.translator.block.exception.SubroutineNotDeclaredException;

public class Ultrasonic extends TranslatorBlock {
    
	public Ultrasonic(Long blockId, Translator translator, String codePrefix, String codeSuffix, String label)
	{
		super(blockId, translator, codePrefix, codeSuffix, label);
	}
	@Override
	public String toCode() throws SocketNullException, SubroutineNotDeclaredException
	{
		TranslatorBlock translatorBlock = this.getRequiredTranslatorBlockAtSocket(0);
		String Pin = translatorBlock.toCode();

		translator.addDefinitionCommand("int measureInCentimeters(int _pin)\n" +
                                                "{\n" +
                                                "	pinMode(_pin, OUTPUT);\n" +
                                                "	digitalWrite(_pin, LOW);\n" +
                                                "	delayMicroseconds(2);\n" +
                                                "	digitalWrite(_pin, HIGH);\n" +
                                                "	delayMicroseconds(5);\n" +
                                                "	digitalWrite(_pin,LOW);\n" +
                                                "	pinMode(_pin,INPUT);\n" +
                                                "	int duration = pulseIn(_pin,HIGH);\n" +
                                                "	return duration/29/2;	\n" +
                                                "}"	);
		
		String ret = "measureInCentimeters("+Pin+")";
		return codePrefix + ret + codeSuffix;
	}
	
	
	
	
	
	
	
	
	
}
