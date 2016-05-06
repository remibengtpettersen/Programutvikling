package test;

import model.StaticGameOfLife;
import model.rules.*;
import model.rules.Rule;
import org.junit.*;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

/**
 * @author Pair programmed.
 *
 * Test class for RuleParser.
 */
public class RuleParserTest {

    private static final String CLASSIC_RULESTRING = "B3/S23";
    private static final String HIGH_LIFE_RULESTRING = "B36/S23";
    private static final String CUSTOM_RULESTRING = "B357/S2468";

    private StaticGameOfLife gol;

    @org.junit.Rule
    public final ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUp() throws Exception {

        gol = new StaticGameOfLife(10, 10);
    }

    /**
     * Verify that test project is correctly configured.
     */
    @Test
    public void testParrot() {
        assertTrue(true);
    }

    @Test
    public void testCreateRule_ifClassicRuleString_thenReturnClassicRuleObject(){

        Rule rule = RuleParser.createRule(gol, CLASSIC_RULESTRING);

        assertTrue(rule instanceof ClassicRule);
    }

    @Test
    public void testCreateRule_ifHighLifeRuleString_thenReturnHighLifeRuleObject(){

        Rule rule = RuleParser.createRule(gol, HIGH_LIFE_RULESTRING);

        assertTrue(rule instanceof HighLifeRule);
    }

    @Test
    public void testCreateRule_ifCustomRuleString_thenReturnCustomRuleObject(){

        Rule rule = RuleParser.createRule(gol, CUSTOM_RULESTRING);

        assertTrue(rule instanceof CustomRule);
    }

    @Test
    public void testFormatRuleText_ifRuleStringIsNonsense_thenThrowException() throws RuleFormatException {

        String rawRulestring = "asfasfmkd";

        expectedException.expect(RuleFormatException.class);
        RuleParser.formatRuleText(rawRulestring);
    }

    @Test
    public void testFormatRuleText_ifRuleStringFormatted_thenReturnSame() throws RuleFormatException {

        String rawRulestring = "B67/S23";
        String formattedRuleString = RuleParser.formatRuleText(rawRulestring);

        assertEquals(rawRulestring, formattedRuleString);
    }

    @Test
    public void testFormatRuleText_ifRuleStringIsLowercase_thenReturnUppercase() throws RuleFormatException {

        String rawRulestring = "b3/s2";
        String expectedRuleString = "B3/S2";

        String formattedRuleString = RuleParser.formatRuleText(rawRulestring);

        assertEquals(expectedRuleString, formattedRuleString);
    }

    @Test
    public void testFormatRuleText_ifRuleStringIsWithoutLetters_thenReturnStandardForm() throws RuleFormatException {

        String rawRulestring = "1/2";           // "Cellebration" notation
        String expectedRuleString = "B2/S1";    // "Golly" notation

        String formattedRuleString = RuleParser.formatRuleText(rawRulestring);

        assertEquals(expectedRuleString, formattedRuleString);
    }

    @Test
    public void testFormatRuleText_ifRuleStringIsWithoutSlash_thenReturnStandardForm() throws RuleFormatException {

        String rawRulestring = "b6s3";
        String expectedRuleString = "B6/S3";

        String formattedRuleString = RuleParser.formatRuleText(rawRulestring);

        assertEquals(expectedRuleString, formattedRuleString);
    }

    @Test
    public void testFormatRuleText_ifRuleStringIsBackwardsWithoutSlash_thenReturnStandardForm() throws RuleFormatException {

        String rawRulestring = "s3b6";
        String expectedRuleString = "B6/S3";

        String formattedRuleString = RuleParser.formatRuleText(rawRulestring);

        assertEquals(expectedRuleString, formattedRuleString);
    }

    @Test
    public void testParseDigitsAfterChar_ifAllDigitsString_thenReturnAllPossibleDigits(){

        String text = "b0123456789"; // the 9 will hopefully be ignored
        char character = 'b';

        boolean[] digits = RuleParser.parseDigitsAfterChar(text, character);
        boolean[] expectedDigits = new boolean[9];

        for (int i = 0; i < expectedDigits.length; i++) {
            expectedDigits[i] = true;
        }

        assertArrayEquals(expectedDigits, digits);
    }

    @Test
    public void testParseDigitsAfterChar_ifNoDigitsString_thenReturnNoDigits(){

        String text = "b";
        char character = 'b';

        boolean[] digits = RuleParser.parseDigitsAfterChar(text, character);
        boolean[] expectedDigits = new boolean[9];

        assertArrayEquals(expectedDigits, digits);
    }

    @Test
    public void testParseDigitsAfterChar_ifRandomDigitsString_thenReturnReasonableDigits(){

        String text = "t15b347942n48";
        char character = 'b';

        boolean[] digits = RuleParser.parseDigitsAfterChar(text, character);
        boolean[] expectedDigits = new boolean[9];

        expectedDigits[2] = true;
        expectedDigits[3] = true;
        expectedDigits[4] = true;
        expectedDigits[7] = true;

        assertArrayEquals(expectedDigits, digits);
    }
}