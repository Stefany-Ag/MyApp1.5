package com.example.myapp15;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.regex.Pattern;

public class MainActivity2 extends  AppCompatActivity {

    TextView tvDatosRFC, tvDatosChino, tvDatosZodiaco;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Bundle bundle = new Bundle();
        bundle = getIntent().getExtras();

        String nombre = bundle.getString(MainActivity.Nombre);
        String apPat = bundle.getString(MainActivity.apellido1);
        String apMat = bundle.getString(MainActivity.apellido2);
        String a = bundle.getString(MainActivity.Anio);
        String m = bundle.getString(MainActivity.Mes);
        String d = bundle.getString(MainActivity.Dia);

        overridePendingTransition(R.anim.sacar, R.anim.mantener);

        int dia = Integer.parseInt(d);
        int mes = Integer.parseInt(m);
        int anio = Integer.parseInt(a);
        String numeros = a.substring(2);
        String letras = primerosCuatroCaracteresRFC(nombre, apPat, apMat);
        String zodiaco = getSigno(mes, dia);
        String chino = getAnimal(anio);
        String rfc = letras+d+m+numeros;

        tvDatosRFC.setText(rfc);
        tvDatosZodiaco.setText(zodiaco);
        tvDatosChino.setText(chino);

    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.mantener, R.anim.sacar);
    }

    //Metodo para regresar
    public void Regresar(View view) {
        Intent vuelve = new Intent(MainActivity2.this, MainActivity.class);
        startActivity(vuelve);
    }

    //Método para el inicio del RFC
    public static String primerosCuatroCaracteresRFC(String nombre, String apellidoPaterno, String apellidoMaterno) {
        //Eliminar acentos y llevar a mayúsculas
        nombre = eliminarAcentosYSimbolos(nombre);
        apellidoPaterno = eliminarAcentosYSimbolos(apellidoPaterno);
        apellidoMaterno = eliminarAcentosYSimbolos(apellidoMaterno);

        //Nombre: Omitir palabras y compuestos que no se utilizan, y obtener las 2 primeras letras
        Pattern pattern = Pattern.compile("\\A(?:(?:MARIA|JOSE) )?+(?:(?:DEL?|L(?:AS?|OS)|M(?:AC|[CI])|V[AO]N|Y)\\b ?)*+([A-Z&]?)([A-Z&]?)");
        final Matcher matcherNom = pattern.matcher(nombre);
        matcherNom.find();

        //Apellido: Omitir palabras que no se utilizan, obtener la primera letra y la vocal interna (si el apellido tiene más de 2 letras)
        pattern = Pattern.compile("\\A(?:(?:DEL?|L(?:AS?|OS)|M(?:AC|[CI])|V[AO]N|Y)\\b ?)*+(([A-Z&]?)[B-DF-HJ-NP-TV-Z&]*([AEIOU]?)[A-Z&]?)");
        final Matcher matcherPat = pattern.matcher(apellidoPaterno);
        matcherPat.find();
        final Matcher matcherMat = pattern.matcher(apellidoMaterno);
        matcherMat.find();

        //LETRAS
        //Obtener vocal de apellido paterno y letra(s) del nombre
        String letraPat = matcherPat.group(2);
        String letraMat = matcherMat.group(2);
        String letraNom = matcherNom.group(1);
        String rfc;
        if (letraPat.isEmpty() || letraMat.isEmpty()) {
            //Si no tiene alguno de los apellidos (paterno o materno), se toma la primera y segunda letra del apellido que tiene y el 4to caracter será la segunda letra del nombre.
            rfc = (matcherPat.group(1) + matcherMat.group(1)).substring(0, 2) + letraNom + matcherNom.group(2);
        } else if (matcherPat.group(1).length() > 2) {
            String vocal = matcherPat.group(3);
            //Cuando el apellido paterno no tiene vocales, se utiliza una X.
            if (vocal.isEmpty())
                vocal = "X";
            rfc = letraPat + vocal + letraMat + letraNom;
        } else {
            //Si el apellido paterno tiene 1 o 2 letras, no se toma la primera vocal, y el 4to caracter es la segunda letra del nombre.
            rfc = letraPat + letraMat + letraNom + matcherNom.group(2);
        }
        //Cuando las 4 letras resulten en una palabra inconveniente, se modifica la última letra a una X
        if (rfc.matches("BUE[IY]|C(?:A[CGK][AO]|O(?:GE|J[AEIO])|ULO)|FETO|GUEY|JOTO|K(?:A(?:[CG][AO]|KA)|O(?:GE|JO)|ULO)|M(?:AM[EO]|E(?:A[RS]|ON)|ION|OCO|ULA)|P(?:E(?:D[AO]|NE)|UT[AO])|QULO|R(?:ATA|UIN)"))
            return rfc.substring(0, 3) + "X";
        else
            return rfc;
    }

    //Si se da el caso de que tenga caracteres especiales
    public static String eliminarAcentosYSimbolos(String s) {
        s = Normalizer.normalize(s.replaceAll("[Ññ]", "&"), Normalizer.Form.NFD);
        s = s.replaceAll("[^&A-Za-z ]", "");
        return s.trim().toUpperCase();
    }

    //Método para obtener el signo zodiacal
    public static String getSigno(int mes, int dia) {
        String signo = "";
        switch (mes) {
            case 1:
                if (dia > 21) {
                    signo = "ACUARIO (Ene 20 - Feb 18)";
                } else {
                    signo = "CAPRICORNIO (Dic 22 - Ene 19)";
                }
                break;
            case 2:
                if (dia > 20) {
                    signo = "PISCIS (Feb 19 - Mar 20)";
                } else {
                    signo = "ACUARIO (Ene 20 - Feb 18)";
                }
                break;
            case 3:
                if (dia > 22) {
                    signo = "ARIES (Mar 21 - Abr 19)";
                } else {
                    signo = "PISCIS (Feb 19 - Mar 20)";
                }
                break;
            case 4:
                if (dia > 21) {
                    signo = "TAURO (Abr 20 - May 20)";
                } else {
                    signo = "ARIES (Mar 21 - Abr 19)";
                }
                break;
            case 5:
                if (dia > 22) {
                    signo = "GEMINIS (May 21 - Jun 20)";
                } else {
                    signo = "TAURO (Abr 20 - May 20)";
                }
                break;
            case 6:
                if (dia > 22) {
                    signo = "CANCER (Jun 21 - Jul 22)";
                } else {
                    signo = "GEMINIS (May 21 - Jun 20)";
                }
                break;
            case 7:
                if (dia > 24) {
                    signo = "LEO (Jul 23 - Ago 22)";
                } else {
                    signo = "CANCER (Jun 21 - Jul 22)";
                }
                break;
            case 8:
                if (dia > 24) {
                    signo = "VIRGO (Ago 23 - Sep 22)";
                } else {
                    signo = "LEO (Jul 23 - Ago 22)";
                }
                break;
            case 9:
                if (dia > 24) {
                    signo = "LIBRA (Sep 23 - Oct 22)";
                } else {
                    signo = "VIRGO (Ago 23 - Sep 22)";
                }
                break;
            case 10:
                if (dia > 24) {
                    signo = "ESCORPION (Oct 23 - Nov 21)";
                } else {
                    signo = "LIBRA (Sep 23 - Oct 22)";
                }
                break;
            case 11:
                if (dia > 23) {
                    signo = "SAGITARIO (Nov 22 - Dic 21)";
                } else {
                    signo = "ESCORPION (Oct 23 - Nov 21)";
                }
                break;
            case 12:
                if (dia > 23) {
                    signo = "CAPRICORNIO (Dic 22 - Ene 19)";
                } else {
                    signo = "SAGITARIO (Nov 22 - Dic 21)";
                }
                break;
        }
        return signo;
    }

    //Método para obtener el año chino
    public static String getAnimal(int anio){
        String animal = "";
        int resto = anio % 12;
        switch (resto) {
            case 0: animal = "Mono"; break;
            case 1: animal = "Gallo"; break;
            case 2: animal = "Perro"; break;
            case 3: animal = "Cerdo"; break;
            case 4: animal = "Rata"; break;
            case 5: animal = "Buey"; break;
            case 6: animal = "Tigre"; break;
            case 7: animal = "Conejo"; break;
            case 8: animal = "Dragon"; break;
            case 9: animal = "Serpiente"; break;
            case 10: animal = "Caballo"; break;
            case 11: animal = "Cabra"; break;
        }
        return animal;
    }
}
