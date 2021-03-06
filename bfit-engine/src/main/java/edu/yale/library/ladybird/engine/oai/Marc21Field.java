package edu.yale.library.ladybird.engine.oai;

import com.google.common.base.Converter;
import com.google.common.base.Enums;

/**
 * @author Osman Din {@literal <osman.din.yale@gmail.com>}
 */
public enum Marc21Field {

    UNK("UNK"),
    _880("880"),
    _245("245"),
    _1("1"),
    _035("035"),
    _020("020"),
    _022("022"),
    _040("040"),
    _041("041"),
    _043("043"),
    _050("050"),
    _066("066"),
    _090("090"),
    _099("099"),
    _100("100"),
    _110("110"),
    _111("111"),
    _130("130"),
    _222("222"),
    _240("240"),
    _242("242"),
    _246("246"),
    _247("247"),
    _250("250"),
    _260("260"),
    _300("300"),
    _310("310"),
    _440("440"),
    _490("490"),
    _500("500"),
    _501("501"),
    _504("504"),
    _505("505"),
    _510("510"),
    _515("515"),
    _520("520"),
    _524("524"),
    _525("525"),
    _533("533"),
    _540("540"),
    _541("541"),
    _545("545"),
    _546("546"),
    _550("550"),
    _555("555"),
    _561("561"),
    _562("562"),
    _580("580"),
    _590("590"),
    _600("600"),
    _610("610"),
    _611("611"),
    _630("630"),
    _650("650"),
    _651("651"),
    _655("655"),
    _690("690"),
    _692("692"),
    _700("700"),
    _710("710"),
    _711("711"),
    _730("730"),
    _740("740"),
    _773("773"),
    _775("775"),
    _776("776"),
    _780("780"),
    _785("785"),
    _787("787"),
    _800("800"),
    _810("810"),
    _811("811"),
    _830("830"),
    _856("856"),
    _693("693"),
    _034("034"),
    _044("044"),
    _362("362"),
    _8("8"),
    _264("264"),
    _852("852");

    String name;

    static String identifierPrefix = "_";

    public static String getIdentifierPrefix() {
        return identifierPrefix;
    }

    private static final Converter<String, Marc21Field> converter = Enums.stringConverter(Marc21Field.class);

    private Marc21Field(final String name) {
        this.name = name;
    }

    public static Marc21Field valueOfTag(final String s) {
        try {
            return converter.convert("_" + s);
        } catch (IllegalArgumentException e) {
            return Marc21Field.UNK;
        }
    }

    /**
     * @param tag marc tag
     * @return Marc21Field corresponding or Marc21Field.UNK if not known
     */
    public static Marc21Field getMar21FieldForString(final String tag) {
        final String TAG_ID = Marc21Field.getIdentifierPrefix(); //fields start with _, e.g. _245
        try {
            Marc21Field marc21Field = Marc21Field.valueOf(TAG_ID + tag);
            return marc21Field;
        } catch (IllegalArgumentException e) {
            //ignore error
            return Marc21Field.UNK;
        }
    }


}
