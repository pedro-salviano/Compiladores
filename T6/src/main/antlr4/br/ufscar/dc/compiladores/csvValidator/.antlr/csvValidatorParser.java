// Generated from /home/pedro/BCC/7sem/compiladores/Compiladores/T6/src/main/antlr4/br/ufscar/dc/compiladores/csvValidator/csvValidator.g4 by ANTLR 4.9.2
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class csvValidatorParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.9.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		DEFINE_STRING=1, TIPO_BASE=2, REGRA=3, NUM_INT_POS=4, NUM_REAL=5, CHECK=6, 
		IDENT=7, DELIM=8, VIRGULA=9, WS=10, AP=11, FP=12, OPERADOR=13, AC=14, 
		FC=15, CADEIA=16, CADEIA_NAO_FECHADA=17, ERRO=18;
	public static final int
		RULE_script = 0, RULE_execucao = 1, RULE_checagem = 2, RULE_definicao = 3, 
		RULE_corpo = 4, RULE_atributo = 5, RULE_tipo = 6, RULE_literal = 7, RULE_tamanho = 8;
	private static String[] makeRuleNames() {
		return new String[] {
			"script", "execucao", "checagem", "definicao", "corpo", "atributo", "tipo", 
			"literal", "tamanho"
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

	@Override
	public String getGrammarFileName() { return "csvValidator.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public csvValidatorParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	public static class ScriptContext extends ParserRuleContext {
		public ExecucaoContext execucao() {
			return getRuleContext(ExecucaoContext.class,0);
		}
		public List<DefinicaoContext> definicao() {
			return getRuleContexts(DefinicaoContext.class);
		}
		public DefinicaoContext definicao(int i) {
			return getRuleContext(DefinicaoContext.class,i);
		}
		public ScriptContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_script; }
	}

	public final ScriptContext script() throws RecognitionException {
		ScriptContext _localctx = new ScriptContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_script);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(19); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(18);
				definicao();
				}
				}
				setState(21); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==IDENT );
			setState(23);
			execucao();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExecucaoContext extends ParserRuleContext {
		public TerminalNode CHECK() { return getToken(csvValidatorParser.CHECK, 0); }
		public TerminalNode AC() { return getToken(csvValidatorParser.AC, 0); }
		public TerminalNode FC() { return getToken(csvValidatorParser.FC, 0); }
		public List<ChecagemContext> checagem() {
			return getRuleContexts(ChecagemContext.class);
		}
		public ChecagemContext checagem(int i) {
			return getRuleContext(ChecagemContext.class,i);
		}
		public ExecucaoContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_execucao; }
	}

	public final ExecucaoContext execucao() throws RecognitionException {
		ExecucaoContext _localctx = new ExecucaoContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_execucao);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(25);
			match(CHECK);
			setState(26);
			match(AC);
			setState(28); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(27);
				checagem();
				}
				}
				setState(30); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==CADEIA );
			setState(32);
			match(FC);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ChecagemContext extends ParserRuleContext {
		public TerminalNode CADEIA() { return getToken(csvValidatorParser.CADEIA, 0); }
		public TerminalNode OPERADOR() { return getToken(csvValidatorParser.OPERADOR, 0); }
		public TerminalNode IDENT() { return getToken(csvValidatorParser.IDENT, 0); }
		public ChecagemContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_checagem; }
	}

	public final ChecagemContext checagem() throws RecognitionException {
		ChecagemContext _localctx = new ChecagemContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_checagem);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(34);
			match(CADEIA);
			setState(35);
			match(OPERADOR);
			setState(36);
			match(IDENT);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DefinicaoContext extends ParserRuleContext {
		public TerminalNode IDENT() { return getToken(csvValidatorParser.IDENT, 0); }
		public TerminalNode AC() { return getToken(csvValidatorParser.AC, 0); }
		public CorpoContext corpo() {
			return getRuleContext(CorpoContext.class,0);
		}
		public TerminalNode FC() { return getToken(csvValidatorParser.FC, 0); }
		public DefinicaoContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_definicao; }
	}

	public final DefinicaoContext definicao() throws RecognitionException {
		DefinicaoContext _localctx = new DefinicaoContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_definicao);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(38);
			match(IDENT);
			setState(39);
			match(AC);
			setState(40);
			corpo();
			setState(41);
			match(FC);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CorpoContext extends ParserRuleContext {
		public List<AtributoContext> atributo() {
			return getRuleContexts(AtributoContext.class);
		}
		public AtributoContext atributo(int i) {
			return getRuleContext(AtributoContext.class,i);
		}
		public CorpoContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_corpo; }
	}

	public final CorpoContext corpo() throws RecognitionException {
		CorpoContext _localctx = new CorpoContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_corpo);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(46);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==IDENT) {
				{
				{
				setState(43);
				atributo();
				}
				}
				setState(48);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AtributoContext extends ParserRuleContext {
		public TerminalNode IDENT() { return getToken(csvValidatorParser.IDENT, 0); }
		public TerminalNode DELIM() { return getToken(csvValidatorParser.DELIM, 0); }
		public TipoContext tipo() {
			return getRuleContext(TipoContext.class,0);
		}
		public TerminalNode REGRA() { return getToken(csvValidatorParser.REGRA, 0); }
		public AtributoContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_atributo; }
	}

	public final AtributoContext atributo() throws RecognitionException {
		AtributoContext _localctx = new AtributoContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_atributo);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(49);
			match(IDENT);
			setState(50);
			match(DELIM);
			setState(51);
			tipo();
			setState(53);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==REGRA) {
				{
				setState(52);
				match(REGRA);
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TipoContext extends ParserRuleContext {
		public LiteralContext literal() {
			return getRuleContext(LiteralContext.class,0);
		}
		public TerminalNode TIPO_BASE() { return getToken(csvValidatorParser.TIPO_BASE, 0); }
		public TipoContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_tipo; }
	}

	public final TipoContext tipo() throws RecognitionException {
		TipoContext _localctx = new TipoContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_tipo);
		try {
			setState(57);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case DEFINE_STRING:
				enterOuterAlt(_localctx, 1);
				{
				setState(55);
				literal();
				}
				break;
			case TIPO_BASE:
				enterOuterAlt(_localctx, 2);
				{
				setState(56);
				match(TIPO_BASE);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class LiteralContext extends ParserRuleContext {
		public TerminalNode DEFINE_STRING() { return getToken(csvValidatorParser.DEFINE_STRING, 0); }
		public TamanhoContext tamanho() {
			return getRuleContext(TamanhoContext.class,0);
		}
		public LiteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_literal; }
	}

	public final LiteralContext literal() throws RecognitionException {
		LiteralContext _localctx = new LiteralContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_literal);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(59);
			match(DEFINE_STRING);
			setState(60);
			tamanho();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TamanhoContext extends ParserRuleContext {
		public TerminalNode AP() { return getToken(csvValidatorParser.AP, 0); }
		public TerminalNode NUM_INT_POS() { return getToken(csvValidatorParser.NUM_INT_POS, 0); }
		public TerminalNode FP() { return getToken(csvValidatorParser.FP, 0); }
		public TamanhoContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_tamanho; }
	}

	public final TamanhoContext tamanho() throws RecognitionException {
		TamanhoContext _localctx = new TamanhoContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_tamanho);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(62);
			match(AP);
			setState(63);
			match(NUM_INT_POS);
			setState(64);
			match(FP);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\24E\4\2\t\2\4\3\t"+
		"\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\3\2\6\2\26"+
		"\n\2\r\2\16\2\27\3\2\3\2\3\3\3\3\3\3\6\3\37\n\3\r\3\16\3 \3\3\3\3\3\4"+
		"\3\4\3\4\3\4\3\5\3\5\3\5\3\5\3\5\3\6\7\6/\n\6\f\6\16\6\62\13\6\3\7\3\7"+
		"\3\7\3\7\5\78\n\7\3\b\3\b\5\b<\n\b\3\t\3\t\3\t\3\n\3\n\3\n\3\n\3\n\2\2"+
		"\13\2\4\6\b\n\f\16\20\22\2\2\2@\2\25\3\2\2\2\4\33\3\2\2\2\6$\3\2\2\2\b"+
		"(\3\2\2\2\n\60\3\2\2\2\f\63\3\2\2\2\16;\3\2\2\2\20=\3\2\2\2\22@\3\2\2"+
		"\2\24\26\5\b\5\2\25\24\3\2\2\2\26\27\3\2\2\2\27\25\3\2\2\2\27\30\3\2\2"+
		"\2\30\31\3\2\2\2\31\32\5\4\3\2\32\3\3\2\2\2\33\34\7\b\2\2\34\36\7\20\2"+
		"\2\35\37\5\6\4\2\36\35\3\2\2\2\37 \3\2\2\2 \36\3\2\2\2 !\3\2\2\2!\"\3"+
		"\2\2\2\"#\7\21\2\2#\5\3\2\2\2$%\7\22\2\2%&\7\17\2\2&\'\7\t\2\2\'\7\3\2"+
		"\2\2()\7\t\2\2)*\7\20\2\2*+\5\n\6\2+,\7\21\2\2,\t\3\2\2\2-/\5\f\7\2.-"+
		"\3\2\2\2/\62\3\2\2\2\60.\3\2\2\2\60\61\3\2\2\2\61\13\3\2\2\2\62\60\3\2"+
		"\2\2\63\64\7\t\2\2\64\65\7\n\2\2\65\67\5\16\b\2\668\7\5\2\2\67\66\3\2"+
		"\2\2\678\3\2\2\28\r\3\2\2\29<\5\20\t\2:<\7\4\2\2;9\3\2\2\2;:\3\2\2\2<"+
		"\17\3\2\2\2=>\7\3\2\2>?\5\22\n\2?\21\3\2\2\2@A\7\r\2\2AB\7\6\2\2BC\7\16"+
		"\2\2C\23\3\2\2\2\7\27 \60\67;";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}