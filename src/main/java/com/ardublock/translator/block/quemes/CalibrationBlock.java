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
public class CalibrationBlock extends TranslatorBlock{

    public CalibrationBlock(Long blockId, Translator translator, String codePrefix, String codeSuffix, String label) {
        super(blockId, translator, codePrefix, codeSuffix, label);
    }

    @Override
    public String toCode() throws SocketNullException, SubroutineNotDeclaredException, BlockException {
        translator.addHeaderFile("QTRSensors.h");
        translator.addHeaderFile("avr/io.h");
        translator.addHeaderFile("avr/interrupt.h");
        
        translator.addDefinitionCommand("#define PINZA                  2     // Pin pinza.\n" +
                                        "#define IZQUIERDA              3     // Pin motor izquierdo.\n" +
                                        "#define DERECHA                14    // Pin motor derecho.\n" + 
                                        "#define NUM_SENSORES           8     // Número de sensores.\n" +
                                        "#define TIMEOUT                2500  // Espera 2500 us hasta que la salida del sensor se ponga en bajo.\n" +
                                        "#define EMISOR                 4     // Emisor controlado en el pin 4.");
        translator.addDefinitionCommand("#define NUM_SENSORES           8");
        translator.addDefinitionCommand("#define UMBRAL                 300");
        
        translator.addDefinitionCommand("#define VMX_LIN_CAL_IZQ_POS    13");
        translator.addDefinitionCommand("#define VMX_LIN_CAL_DER_POS    13");
        
        translator.addDefinitionCommand("#define VMX_LIN_CAL_IZQ_NEG    -7 ");
        translator.addDefinitionCommand("#define VMX_LIN_CAL_DER_NEG    -13 ");
        
        translator.addDefinitionCommand("#define VMX_LIN_IZQ_POS        13");
        translator.addDefinitionCommand("#define VMX_LIN_DER_POS        13");
        
        translator.addDefinitionCommand("#define VMX_GIROIZQ_IZQ_POS    5");
        translator.addDefinitionCommand("#define VMX_GIROIZQ_DER_NEG    -5");
        
        translator.addDefinitionCommand("#define VMX_GIRODER_DER_POS    4");
        translator.addDefinitionCommand("#define VMX_GIRODER_IZQ_NEG    -3");
        
        translator.addDefinitionCommand("QTRSensorsRC qtrrc((unsigned char[]) {12, 11, 10, 9, 8, 7, 6, 5}, NUM_SENSORES, TIMEOUT, EMISOR);");
        translator.addDefinitionCommand("unsigned int valorSensores[8];");
        translator.addDefinitionCommand("int velIzq = 0;");
        translator.addDefinitionCommand("int velDer = 0;");
        translator.addDefinitionCommand("int estado          =          0;   \n" +
                                        "int subestado       =          0;   \n" );
        translator.addDefinitionCommand("void inicializaTimer(int c){  \n" +
                                        "    cli();             \n" +
                                        "    TCCR1A = 0;        \n" +
                                        "    TCCR1B = 0;			\n" +
                                        "    OCR1A = c;               \n" +
                                        "    TCCR1B |= (1 << WGM12); \n" +
                                        "    TCCR1B |= (1 << CS10);    \n" +
                                        "    TIMSK1 = (1 << OCIE1A);\n" +
                                        "    sei(); \n" +
                                        "}\n");
        translator.addDefinitionCommand("ISR(TIMER1_COMPA_vect){\n" +
                                        "    static int contDerecha;\n" +
                                        "    static int contIzquierda;    \n" +
                                        "    contDerecha++;        \n" +
                                        "    if(contDerecha>=60-velDer){\n" +
                                        "      digitalWrite(14, !digitalRead(14));\n" +
                                        "      contDerecha=0;\n" +
                                        "    }\n" +
                                        "    contIzquierda++;    \n" +
                                        "    if(contIzquierda>=60+velIzq){\n" +
                                        "      digitalWrite(3, !digitalRead(3));\n" +
                                        "      contIzquierda=0;\n" +
                                        "    }\n" +
                                        "}\n");
        translator.addDefinitionCommand("void sigueLinea(){ \n" +
                                    "    unsigned int pos = qtrrc.readLine(valorSensores);\n" +
                                    "    for(int i=0;i<NUM_SENSORES;i++){\n" +
                                    "      if(valorSensores[i]>=UMBRAL){\n" +
                                    "        valorSensores[i]=1000;\n" +
                                    "      } \n" +
                                    "    }\n" +
                                    "\n" +
                                    "    if(pos<=100){ \n" +
                                    "      velIzq=0;\n" +
                                    "      velDer=0; \n" +
                                    "    }\n" +
                                    "    else{\n" +
                                    "      if(((valorSensores[0]+valorSensores[1]>=1000) && (valorSensores[4]+valorSensores[5]>=1000))|| sobreCruce()){\n" +
                                    "        // En línea.\n" +
                                    "        velIzq=VMX_LIN_IZQ_POS;\n" +
                                    "        velDer=VMX_LIN_DER_POS; \n" +
                                    "      }\n" +
                                    "      else{\n" +
                                    "        if(valorSensores[0]>valorSensores[1] ){\n" +
                                    "          // Serial.println(\"Salido a derecha\"); \n" +
                                    "          velIzq=VMX_LIN_IZQ_POS;\n" +
                                    "          velDer=0; \n" +
                                    "        }\n" +
                                    "        else{\n" +
                                    "          if(valorSensores[0]<valorSensores[1]){\n" +
                                    "            //  Serial.println(\"Salido a izquierda\"); \n" +
                                    "            velIzq=0;\n" +
                                    "            velDer=VMX_LIN_DER_POS; \n" +
                                    "          }\n" +
                                    "          else{\n" +
                                    "            if(valorSensores[2]+valorSensores[3]+valorSensores[4]>valorSensores[5]+valorSensores[6]+valorSensores[7]){\n" +
                                    "              //   Serial.println(\"Salido a izquierda\"); \n" +
                                    "              velIzq=0;\n" +
                                    "              velDer=VMX_LIN_DER_POS; \n" +
                                    "            }\n" +
                                    "            else{\n" +
                                    "              //  Serial.println(\"Salido a derecha\"); \n" +
                                    "              if(valorSensores[2]+valorSensores[3]+valorSensores[4]<valorSensores[5]+valorSensores[6]+valorSensores[7]){\n" +
                                    "                velIzq=VMX_LIN_IZQ_POS;\n" +
                                    "                velDer=0;\n" +
                                    "              }\n" +
                                    "            } \n" +
                                    "          }\n" +
                                    "        }\n" +
                                    "      }\n" +
                                    "  }\n" +
                                    "}");
        translator.addDefinitionCommand("boolean sobreCruce(){\n" +
                                "  int acumulaSensores=0;\n" +
                                "  \n" +
                                "  for (unsigned char i = 0; i < NUM_SENSORES; i++)\n" +
                                "  {\n" +
                                "    acumulaSensores+=valorSensores[i];\n" +
                                "  }\n" +
                                "  \n" +
                                "  if(acumulaSensores>=7000 && (valorSensores[0]==1000 || valorSensores[1]==1000)){  \n" +
                                "    return true;\n" +
                                "  }\n" +
                                "  \n" +
                                "  else\n" +
                                "    return false;\n" +
                                "}\n");
        translator.addDefinitionCommand("boolean enCruceG(){\n" +
                                        "if(valorSensores[0]+valorSensores[1]>=2000)  \n" +
                                        "    return true;\n" +
                                        "  else\n" +
                                        "    return false;\n" +
                                        "}");
        translator.addDefinitionCommand("void giraDerecha(){\n" +
                                        "  unsigned int pos = qtrrc.readLine(valorSensores);\n" +
                                        "    for(int i=0;i<NUM_SENSORES;i++){\n" +
                                        "      if(valorSensores[i]>=UMBRAL){\n" +
                                        "        valorSensores[i]=1000;\n" +
                                        "      } \n" +
                                        "    }\n" +
                                        "  velIzq=VMX_GIRODER_IZQ_NEG;\n" +
                                        "  velDer=VMX_GIRODER_DER_POS; \n" +
                                        "}");
        translator.addDefinitionCommand("void giraIzquierda(){\n" +
                                        "  unsigned int pos = qtrrc.readLine(valorSensores);\n" +
                                        "    for(int i=0;i<NUM_SENSORES;i++){\n" +
                                        "      if(valorSensores[i]>=UMBRAL){\n" +
                                        "        valorSensores[i]=1000;\n" +
                                        "      } \n" +
                                        "    }\n" +
                                        "  velIzq=VMX_GIROIZQ_IZQ_POS;\n" +
                                        "  velDer=VMX_GIROIZQ_DER_NEG; \n" +
                                        "}");
        
        String ret = "delay(7000);\n" +
                    "  inicializaTimer(400);\n" +
                    "  \n" +
                
                    "  // Inicialización de los motores.\n" +
                    "  pinMode(IZQUIERDA, OUTPUT);\n" +
                    "  pinMode(DERECHA, OUTPUT);\n" +
                    "  pinMode(PINZA, OUTPUT);\n" +
                    "   \n" +
                    "  // Calibración de los sensores.\n" +
                    "  delay(500); \n" +
                    "  pinMode(13, OUTPUT);\n" +
                    "  \n" +
                    "  // Indica que está en modo de calibración.\n" +
                    "  digitalWrite(13,HIGH);\n" +
                    "   velIzq=10;\n" +
                    "   velDer=8;\n" +
                    "  for (int i = 0; i < 400; i++)  \n" +
                    "  {\n" +
                    "    // Lee todos los sensores 10 veces a 2500 us por lectura (~25 ms por llamada).\n" +
                    "    qtrrc.calibrate();\n" +
                    "    if((i/50)%2==0){\n" +
                    "      velIzq=VMX_LIN_CAL_IZQ_POS;\n" +
                    "      velDer=VMX_LIN_CAL_DER_POS;      \n" +
                    "    }\n" +
                    "    else{\n" +
                    "      velIzq=VMX_LIN_CAL_IZQ_NEG;\n" +
                    "      velDer=VMX_LIN_CAL_DER_NEG;  \n" +
                    "    }\n" +
                    "  }\n" +
                    "  // Indica que finalizó modo de calibración.\n" +
                    "  digitalWrite(13,LOW);\n" +
                    "  // Modificar según la velocidad del bluetooth usado.  \n" +
                    "  Serial.begin(9600); \n" +
                    "  velIzq=0;\n" +
                    "  velDer=0;\n" + 
                    "  delay( 5000 );\n";
        
        return ret;
    }
    
}
