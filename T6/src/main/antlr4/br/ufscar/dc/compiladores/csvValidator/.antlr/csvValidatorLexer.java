// Generated from /home/pedro/BCC/7sem/compiladores/Compiladores/T6/src/main/antlr4/br/ufscar/dc/compiladores/csvValidator/csvValidator.g4 by ANTLR 4.9.2
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class csvValidatorLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.9.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		DEFINE_STRING=1, TIPO_BASE=2, REGRA=3, NUM_INT_POS=4, NUM_REAL=5, CHECK=6, 
		IDENT=7, DELIM=8, VIRGULA=9, WS=10, AP=11, FP=12, OPERADOR=13, AC=14, 
		FC=15, CADEIA=16, CADEIA_NAO_FECHADA=17, ERRO=18;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"DEFINE_STRING", "TIPO_BASE", "REGRA", "NUM_INT_POS", "NUM_REAL", "DIGITO", 
			"CHECK", "IDENT", "DELIM", "VIRGULA", "WS", "AP", "FP", "OPERADOR", "AC", 
			"FC", "CADEIA", "CADEIA_NAO_FECHADA", "ESC_SEQ", "ERRO"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'string'", null, "'PK'", null, null, "'verificar'", null, "':'", 
			"','", null, "'('", "')'", "'->'", "'{'", "'}'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "DEFINE_STRING", "TIPO_BASE", "REGRA", "NUM_INT_POS", "NUM_REAL", 
			"CHECK", "IDENT", "DELIM", "VIRGULA", "WS", "AP", "FP", "OPERADOR", "AC", 
			"FC", "CADEIA", "CADEIA_NAO_FECHADA", "ERRO"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}


	public csvValidatorLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "csvValidator.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	@Override
	public void action(RuleContext _localctx, int ruleIndex, int actionIndex) {
		switch (ruleIndex) {
		case 10:
			WS_action((RuleContext)_localctx, actionIndex);
			break;
		}
	}
	private void WS_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 0:
			 skip(); 
			break;
		}
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\24\u009d\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\4\25\t\25\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\5\3F\n\3\3\4\3\4\3\4\3\5\6\5L\n\5\r\5\16\5M\3\6\6\6Q\n\6\r\6\16\6R\3"+
		"\6\6\6V\n\6\r\6\16\6W\3\6\6\6[\n\6\r\6\16\6\\\3\7\3\7\3\b\3\b\3\b\3\b"+
		"\3\b\3\b\3\b\3\b\3\b\3\b\3\t\6\tl\n\t\r\t\16\tm\3\n\3\n\3\13\3\13\3\f"+
		"\3\f\3\f\3\r\3\r\3\16\3\16\3\17\3\17\3\17\3\20\3\20\3\21\3\21\3\22\3\22"+
		"\3\22\7\22\u0085\n\22\f\22\16\22\u0088\13\22\3\22\3\22\3\23\3\23\3\23"+
		"\7\23\u008f\n\23\f\23\16\23\u0092\13\23\3\23\3\23\3\24\3\24\3\24\3\24"+
		"\5\24\u009a\n\24\3\25\3\25\2\2\26\3\3\5\4\7\5\t\6\13\7\r\2\17\b\21\t\23"+
		"\n\25\13\27\f\31\r\33\16\35\17\37\20!\21#\22%\23\'\2)\24\3\2\6\5\2\62"+
		";C\\c|\5\2\13\f\17\17\"\"\4\2$$))\6\2\f\f$$))^^\2\u00a6\2\3\3\2\2\2\2"+
		"\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\17\3\2\2\2\2\21\3\2"+
		"\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2"+
		"\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2)\3\2\2\2"+
		"\3+\3\2\2\2\5E\3\2\2\2\7G\3\2\2\2\tK\3\2\2\2\13P\3\2\2\2\r^\3\2\2\2\17"+
		"`\3\2\2\2\21k\3\2\2\2\23o\3\2\2\2\25q\3\2\2\2\27s\3\2\2\2\31v\3\2\2\2"+
		"\33x\3\2\2\2\35z\3\2\2\2\37}\3\2\2\2!\177\3\2\2\2#\u0081\3\2\2\2%\u008b"+
		"\3\2\2\2\'\u0099\3\2\2\2)\u009b\3\2\2\2+,\7u\2\2,-\7v\2\2-.\7t\2\2./\7"+
		"k\2\2/\60\7p\2\2\60\61\7i\2\2\61\4\3\2\2\2\62\63\7k\2\2\63\64\7p\2\2\64"+
		"\65\7v\2\2\65\66\7g\2\2\66\67\7k\2\2\678\7t\2\28F\7q\2\29:\7t\2\2:;\7"+
		"g\2\2;<\7c\2\2<F\7n\2\2=>\7d\2\2>?\7q\2\2?@\7q\2\2@A\7n\2\2AB\7g\2\2B"+
		"C\7c\2\2CD\7p\2\2DF\7q\2\2E\62\3\2\2\2E9\3\2\2\2E=\3\2\2\2F\6\3\2\2\2"+
		"GH\7R\2\2HI\7M\2\2I\b\3\2\2\2JL\5\r\7\2KJ\3\2\2\2LM\3\2\2\2MK\3\2\2\2"+
		"MN\3\2\2\2N\n\3\2\2\2OQ\5\r\7\2PO\3\2\2\2QR\3\2\2\2RP\3\2\2\2RS\3\2\2"+
		"\2SU\3\2\2\2TV\7\60\2\2UT\3\2\2\2VW\3\2\2\2WU\3\2\2\2WX\3\2\2\2XZ\3\2"+
		"\2\2Y[\5\r\7\2ZY\3\2\2\2[\\\3\2\2\2\\Z\3\2\2\2\\]\3\2\2\2]\f\3\2\2\2^"+
		"_\4\62;\2_\16\3\2\2\2`a\7x\2\2ab\7g\2\2bc\7t\2\2cd\7k\2\2de\7h\2\2ef\7"+
		"k\2\2fg\7e\2\2gh\7c\2\2hi\7t\2\2i\20\3\2\2\2jl\t\2\2\2kj\3\2\2\2lm\3\2"+
		"\2\2mk\3\2\2\2mn\3\2\2\2n\22\3\2\2\2op\7<\2\2p\24\3\2\2\2qr\7.\2\2r\26"+
		"\3\2\2\2st\t\3\2\2tu\b\f\2\2u\30\3\2\2\2vw\7*\2\2w\32\3\2\2\2xy\7+\2\2"+
		"y\34\3\2\2\2z{\7/\2\2{|\7@\2\2|\36\3\2\2\2}~\7}\2\2~ \3\2\2\2\177\u0080"+
		"\7\177\2\2\u0080\"\3\2\2\2\u0081\u0086\t\4\2\2\u0082\u0085\5\'\24\2\u0083"+
		"\u0085\n\5\2\2\u0084\u0082\3\2\2\2\u0084\u0083\3\2\2\2\u0085\u0088\3\2"+
		"\2\2\u0086\u0084\3\2\2\2\u0086\u0087\3\2\2\2\u0087\u0089\3\2\2\2\u0088"+
		"\u0086\3\2\2\2\u0089\u008a\t\4\2\2\u008a$\3\2\2\2\u008b\u0090\t\4\2\2"+
		"\u008c\u008f\5\'\24\2\u008d\u008f\n\5\2\2\u008e\u008c\3\2\2\2\u008e\u008d"+
		"\3\2\2\2\u008f\u0092\3\2\2\2\u0090\u008e\3\2\2\2\u0090\u0091\3\2\2\2\u0091"+
		"\u0093\3\2\2\2\u0092\u0090\3\2\2\2\u0093\u0094\7\f\2\2\u0094&\3\2\2\2"+
		"\u0095\u0096\7^\2\2\u0096\u009a\7)\2\2\u0097\u0098\7^\2\2\u0098\u009a"+
		"\7p\2\2\u0099\u0095\3\2\2\2\u0099\u0097\3\2\2\2\u009a(\3\2\2\2\u009b\u009c"+
		"\13\2\2\2\u009c*\3\2\2\2\16\2EMRW\\m\u0084\u0086\u008e\u0090\u0099\3\3"+
		"\f\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}