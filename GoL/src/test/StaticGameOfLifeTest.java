//package test;
//
//import model.StaticGameOfLife;
//import org.junit.*;
//import org.junit.rules.ExpectedException;
//
//import static org.junit.Assert.*;
//
///**
// * Created by Andreas on 29.02.2016.
// */
//@Deprecated
//public class StaticGameOfLifeTest {
//
//    @Rule
//    public ExpectedException expectedException = ExpectedException.none();
//
//    static StaticGameOfLife gol;
//    static boolean [][] grid;
//
//    @BeforeClass
//    public static void setUp() throws Exception {
//        gol = new StaticGameOfLife(5, 5);
//    }
//
//    @After
//    public void tearDown() throws Exception {
//
//    }
//
//    @Test
//    public void testCreateGameBoard(){
//        StaticGameOfLife staticGameofLife = new StaticGameOfLife(5, 5);
//        assertEquals(5, staticGameofLife.getGrid().length);
//        assertEquals(5, staticGameofLife.getGrid()[0].length);
//        assertEquals(5, staticGameofLife.getNeighbours().length);
//        assertEquals(5, staticGameofLife.getNeighbours()[0].length);
//    }
//
//    @Test
//    public void testAggregateNeighbours() throws Exception {
//        grid = new boolean[][]{
//                new boolean[]{false, false, false, false, false},
//                new boolean[]{false, false, false, false, false},
//                new boolean[]{false, true, true, true, false},
//                new boolean[]{false, false, false, false, false},
//                new boolean[]{false, false, false, false, false}};
//
//        gol.setGol(grid);
//        gol.aggregateNeighbours();
//        assertArrayEquals(new byte[][]{
//                new byte[]{0, 0, 0, 0, 0},
//                new byte[]{1, 2, 3, 2, 1},
//                new byte[]{1, 1, 2, 1, 1},
//                new byte[]{1, 2, 3, 2, 1},
//                new byte[]{0, 0, 0, 0, 0}}, gol.getNeighbours());
//    }
//
//    @Test
//    public void testIfRuleIsSetToClassicThenSetClassicRuleSet() {
//        gol.setRule("classic");
//        assertEquals("B3/S23", gol.getRule().toString());
//    }
//
//    @Test
//    public void testIfRuleIsSetToHighLifeThenSetHighlifeRuleSet() {
//        gol.setRule("highlife");
//        assertEquals("B36/S23", gol.getRule().toString());
//    }
//
//    @Test
//    public void testIfRuleIsSetToCustomThenSetCustomRuleSet () {
//        gol.setRule("B7/S28");
//        assertEquals("B7/S28", gol.getRule().toString());
//    }
//
//    @Test
//    public void testIfClearGirdThenEmptyGrid() {
//
//        boolean[][] testGrid = new boolean[3][3];
//        testGrid[1][1] = true;
//
//        gol.setGol(testGrid);
//        gol.clearGrid();
//
//        for (int x = 0; x < gol.getGrid().length; x++) {
//            for (int y = 0; y < gol.getGrid()[0].length; y++) {
//                assertFalse(gol.getGrid()[x][y]);
//            }
//        }
//    }
//
//    @Test
//    public void testIfOneCellIsAliveThenNextGenerationSetCellDead() {
//        StaticGameOfLife staticGameOfLife = new StaticGameOfLife(4, 4);
//        staticGameOfLife.setCellAlive(1, 1);
//        staticGameOfLife.aggregateNeighbours();
//
//        staticGameOfLife.nextGeneration();
//        assertFalse(staticGameOfLife.getGrid()[1][1]);
//    }
//}