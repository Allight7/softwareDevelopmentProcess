package ru.ifmo.sdp.matrix;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Random;

/**
 * User: allight
 * Date: 15.01.2016 1:56
 */
public class MatrixUnitTest extends Assert {

    private double[][] testData;
    private double[][] testDataTransposed;
    private double[][] testDataMulTransposed;
    private double[][] testDataTransposedMulBase;

    @Before
    public void setTestData() {
        testData = new double[][]
                {
                        {4, 3, 2, 1},
                        {8, 7, 6, 5}
                };
        testDataTransposed = new double[][]
                {
                        {4, 8},
                        {3, 7},
                        {2, 6},
                        {1, 5}
                };
        testDataMulTransposed = new double[][]
                {
                        {30, 70},
                        {70, 174}
                };
        testDataTransposedMulBase = new double[][]
                {
                        {80, 68, 56, 44},
                        {68, 58, 48, 38},
                        {56, 48, 40, 32},
                        {44, 38, 32, 26},
                };

    }

    /**
     * Проверка на наличие исключения при создании вектора неверного размера
     */
    @Test(expected = IllegalArgumentException.class)
    public void constructNullVectorByBadSizes() {
        new Matrix(0);
    }

    /**
     * Проверка корректности размеров создаваемого вектора
     */
    @Test
    public void constructVectorBySizes() {
        final int length = 91;
        final int expectedY = 1;
        Matrix m = new Matrix(length);
        assertEquals(length, m.rows());
        assertEquals(length, m.getDataClone().length);
        assertEquals(expectedY, m.columns());
        assertEquals(expectedY, m.getDataClone()[0].length);
    }

    /**
     * Проверка на наличие исключения при создании матрицы неверного размера
     */
    @Test(expected = IllegalArgumentException.class)
    public void constructMatrixByBadSizes() {
        new Matrix(0, 0);
    }

    /**
     * Проверка корректности размеров создаваемой матрицы
     */
    @Test
    public void constructMatrixBySizes() {
        final int expectedX = 12;
        final int expectedY = 11;
        Matrix m = new Matrix(expectedX, expectedY);
        assertEquals(expectedX, m.rows());
        assertEquals(expectedX, m.getDataClone().length);
        assertEquals(expectedY, m.columns());
        assertEquals(expectedY, m.getDataClone()[0].length);
    }

    /**
     * Проверка на наличие исключения при создании матрицы от массива неверного размера
     */
    @Test(expected = IllegalArgumentException.class)
    public void constructMatrixByBadSizedData() {
        new Matrix(new double[1][0]);
    }

    /**
     * Проверка на наличие исключения при создании матрицы от непрямоугольного массива
     */
    @Test(expected = IllegalArgumentException.class)
    public void constructMatrixByNoRectangleData() {
        new Matrix(new double[][]{{1, 2}, {}, {1}});
    }

    /**
     * Проверка корректности и независимости создаваемой матрицы от исходного массива данных
     */
    @Test
    public void constructMatrixByData() {
        final int expectedX = testData.length;
        final int expectedY = testData[0].length;
        Matrix m = new Matrix(testData);
        assertEquals(expectedX, m.rows());
        assertEquals(expectedY, m.columns());
        assertArrayEquals(testData, m.getDataClone());
        testData[0][0]++;
        assertFalse(Arrays.deepEquals(testData, m.getDataClone()));
    }

    /**
     * Проверка корректности и независимости создаваемой через конструктор копирования матрицы
     */
    @Test
    public void constructCopyMatrix() {
        Matrix m = new Matrix(testData);
        Matrix n = new Matrix(m);
        assertEquals(m.rows(), n.rows());
        assertEquals(m.columns(), n.columns());
        assertArrayEquals(m.getDataClone(), n.getDataClone());
        m.setElement(0, 0, -1);
        assertFalse(Arrays.deepEquals(m.getDataClone(), n.getDataClone()));
    }

    /**
     * Проверка сохранения инкапсуляции при выдаче массива данных
     */
    @Test
    public void getDataCopy() {
        Matrix m = new Matrix(testData);
        double[][] mDataCopy = m.getDataClone();
        mDataCopy[0][0]++;
        assertFalse(Arrays.deepEquals(mDataCopy, m.getDataClone()));
    }

    /**
     * Проверка на наличие исключения при запросе элемента матрицы с индексом строки меньше 0
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void getElementByBadRowIndexLow() {
        new Matrix(1, 1).getElement(-1, 0);
    }

    /**
     * Проверка на наличие исключения при запросе элемента матрицы с индексом строки не меньше <code>x</code>
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void getElementByBadRowIndexHigh() {
        new Matrix(1, 1).getElement(1, 0);
    }

    /**
     * Проверка на наличие исключения при запросе элемента матрицы с индексом столбца меньше 0
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void getElementByBadColumnIndexLow() {
        new Matrix(1, 1).getElement(0, -1);
    }

    /**
     * Проверка на наличие исключения при запросе элемента матрицы с индексом столбца не меньше <code>y</code>
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void getElementByBadColumnIndexHigh() {
        new Matrix(1, 1).getElement(0, 1);
    }

    /**
     * Проверка корректности получаемых по индексам элементов матрицы
     */
    @Test
    public void getElementForMatrix() {
        Matrix m = new Matrix(testData);
        double delta = 0;
        for (int i = 0; i < testData.length; i++)
            for (int j = 0; j < testData[0].length; j++)
                assertEquals(testData[i][j], m.getElement(i, j), delta);
    }

    /**
     * Проверка на наличие исключения при запросе элемента матрицы с индексом строки меньше 0
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void setElementByBadRowIndexLow() {
        new Matrix(1, 1).setElement(-1, 0, .0);
    }

    /**
     * Проверка на наличие исключения при запросе элемента матрицы с индексом строки не меньше <code>x</code>
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void setElementByBadRowIndexHigh() {
        new Matrix(1, 1).setElement(1, 0, .0);
    }

    /**
     * Проверка на наличие исключения при запросе элемента матрицы с индексом столбца меньше 0
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void setElementByBadColumnIndexLow() {
        new Matrix(1, 1).setElement(0, -1, .0);
    }

    /**
     * Проверка на наличие исключения при запросе элемента матрицы с индексом столбца не меньше <code>y</code>
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void setElementByBadColumnIndexHigh() {
        new Matrix(1, 1).setElement(0, 1, .0);
    }

    /**
     * Проверка корректности задания значения элементов по индексам  матрицы
     */
    @Test
    public void setElementForMatrix() {
        Matrix m = new Matrix(testData.length, testData[0].length);
        for (int i = 0; i < m.rows(); i++)
            for (int j = 0; j < m.columns(); j++)
                m.setElement(i, j, testData[i][j]);
        assertArrayEquals(testData, m.getDataClone());
    }


    /**
     * Проверка на наличие исключения при запросе элемента по одному индексу, если задано больше одного столбца
     */
    @Test(expected = IllegalStateException.class)
    public void getElementByByOnlyRowIndexForMatrix() {
        new Matrix(testData).getElement(0);
    }

    /**
     * Проверка на наличие исключения при запросе элемента вектора с индексом меньше 0
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void getElementByBadRowIndexLowForVector() {
        new Matrix(1).getElement(-1);
    }

    /**
     * Проверка на наличие исключения при запросе элемента вектора с индексом не меньше <code>x</code>
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void getElementByBadRowIndexHighForVector() {
        new Matrix(1).getElement(1);
    }

    /**
     * Проверка на наличие исключения при запросе элемента по одному индексу, если задано больше одного столбца
     */
    @Test(expected = IllegalStateException.class)
    public void setElementByByOnlyRowIndexForMatrix() {
        new Matrix(testData).setElement(0, .0);
    }

    /**
     * Проверка на наличие исключения при запросе элемента вектора с индексом меньше 0
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void setElementByBadRowIndexLowForVector() {
        new Matrix(1).setElement(-1, .0);
    }

    /**
     * Проверка на наличие исключения при запросе элемента вектора с индексом не меньше <code>x</code>
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void setElementByBadRowIndexHighForVector() {
        new Matrix(1).setElement(1, .0);
    }

    /**
     * Проверка корректности задаваемых и получаемых по индексу элементов вектора
     */
    @Test
    public void setAndGetElementForVector() {
        Matrix m = new Matrix(testData.length, 1);
        for (int i = 0; i < testData.length; i++)
            m.setElement(i, testData[i][0]);
        double delta = 0;
        for (int i = 0; i < testData.length; i++)
            assertEquals(testData[i][0], m.getElement(i), delta);
    }

    /**
     * Проверка на наличие исключения при суммировании матриц разного размера
     */
    @Test(expected = IllegalArgumentException.class)
    public void elementWiseDifferentSizes() {
        Matrix.matrixElementWiseOperation(new Matrix(1, 1), new Matrix(2, 2), Matrix.Operation.SUM);
    }

    /**
     * Проверка корректности суммирования (вычетания) матриц статическим методом
     */
    @Test
    public void sumSubtractMatricesStatic() {
        Matrix m = new Matrix(testData);
        Matrix m2 = Matrix.matrixElementWiseOperation(m, m, Matrix.Operation.SUM);
        double delta = 0;
        for (int i = 0; i < testData.length; i++)
            for (int j = 0; j < testData[0].length; j++)
                assertEquals(m2.getElement(i, j), testData[i][j] * 2, delta);
        Matrix m1 = Matrix.matrixElementWiseOperation(m2, m, Matrix.Operation.SUBTRACT);
        assertArrayEquals(m.getDataClone(), m1.getDataClone());
    }

    /**
     * Проверка корректности суммирования (вычетания) матриц
     */
    @Test
    public void sumSubtractMatrices() {
        Matrix m = new Matrix(testData);
        Matrix m2 = m.addMatrix(m);
        double delta = 0;
        for (int i = 0; i < testData.length; i++)
            for (int j = 0; j < testData[0].length; j++)
                assertEquals(m2.getElement(i, j), testData[i][j] * 2, delta);
        Matrix m1 = m2.subMatrix(m);
        assertArrayEquals(m.getDataClone(), m1.getDataClone());
    }

    /**
     * Проверка корректности поэлементного умножения (деления) матриц статическим методом
     */
    @Test
    public void multiplyDivideElementWiseMatricesStatic() {
        Matrix m = new Matrix(testData);
        Matrix m2 = Matrix.matrixElementWiseOperation(m, m, Matrix.Operation.MULTIPLY);
        double delta = 0;
        for (int i = 0; i < testData.length; i++)
            for (int j = 0; j < testData[0].length; j++)
                assertEquals(m2.getElement(i, j), testData[i][j] * testData[i][j], delta);
        Matrix m1 = Matrix.matrixElementWiseOperation(m2, m, Matrix.Operation.DIVIDE);
        assertArrayEquals(m.getDataClone(), m1.getDataClone());
    }

    /**
     * Проверка корректности поэлементного умножения (деления) матриц
     */
    @Test
    public void multiplyDivideElementWiseMatrices() {
        Matrix m = new Matrix(testData);
        Matrix m2 = m.mulMatrixElementWise(m);
        double delta = 0;
        for (int i = 0; i < testData.length; i++)
            for (int j = 0; j < testData[0].length; j++)
                assertEquals(m2.getElement(i, j), testData[i][j] * testData[i][j], delta);
        Matrix m1 = m2.divMatrixElementWise(m);
        assertArrayEquals(m.getDataClone(), m1.getDataClone());
    }

    /**
     * Проверка на наличие исключения при перемножении матриц с недопустимыми размера
     */
    @Test(expected = IllegalArgumentException.class)
    public void multiplyDifferentSizes() {
        Matrix.matrixMultiply(new Matrix(1, 1), new Matrix(2, 2));
    }

    /**
     * Проверка корректности умножения матриц статическим методом
     */
    @Test
    public void multiplyMatricesStatic() {
        Matrix m1 = Matrix.matrixMultiply(new Matrix(testData), new Matrix(testDataTransposed));
        assertArrayEquals(m1.getDataClone(), testDataMulTransposed);
        Matrix m2 = Matrix.matrixMultiply(new Matrix(testDataTransposed), new Matrix(testData));
        assertArrayEquals(m2.getDataClone(), testDataTransposedMulBase);
    }

    /**
     * Проверка корректности умножения матриц
     */
    @Test
    public void multiplyMatrices() {
        Matrix m1 = new Matrix(testData).mulMatrix(new Matrix(testDataTransposed));
        assertArrayEquals(m1.getDataClone(), testDataMulTransposed);
        Matrix m2 = new Matrix(testDataTransposed).mulMatrix(new Matrix(testData));
        assertArrayEquals(m2.getDataClone(), testDataTransposedMulBase);
    }

    /**
     * Проверка корректности суммирования (вычетания) матрицы со скаляром статическим методом
     */
    @Test
    public void sumSubtractScalarStatic() {
        Matrix m = new Matrix(testData);
        double addition = 3;
        Matrix m2 = Matrix.scalarOperation(m, addition, Matrix.Operation.SUM);
        double delta = 0;
        for (int i = 0; i < testData.length; i++)
            for (int j = 0; j < testData[0].length; j++)
                assertEquals(m2.getElement(i, j), testData[i][j] + addition, delta);
        Matrix m1 = Matrix.scalarOperation(m2, addition, Matrix.Operation.SUBTRACT);
        assertArrayEquals(m.getDataClone(), m1.getDataClone());
    }

    /**
     * Проверка корректности суммирования (вычетания) матрицы со скаляром
     */
    @Test
    public void sumSubtractScalar() {
        Matrix m = new Matrix(testData);
        double addition = 3;
        Matrix m2 = m.addScalar(addition);
        double delta = 0;
        for (int i = 0; i < testData.length; i++)
            for (int j = 0; j < testData[0].length; j++)
                assertEquals(m2.getElement(i, j), testData[i][j] + addition, delta);
        Matrix m1 = m2.subScalar(addition);
        assertArrayEquals(m.getDataClone(), m1.getDataClone());
    }

    /**
     * Проверка корректности поэлементного умножения (деления) матрицы со скаляром статическим методом
     */
    @Test
    public void multiplyDivideScalarStatic() {
        Matrix m = new Matrix(testData);
        double multiplier = 3;
        Matrix m2 = Matrix.scalarOperation(m, multiplier, Matrix.Operation.MULTIPLY);
        double delta = 0;
        for (int i = 0; i < testData.length; i++)
            for (int j = 0; j < testData[0].length; j++)
                assertEquals(m2.getElement(i, j), testData[i][j] * multiplier, delta);
        Matrix m1 = Matrix.scalarOperation(m2, multiplier, Matrix.Operation.DIVIDE);
        assertArrayEquals(m.getDataClone(), m1.getDataClone());
    }

    /**
     * Проверка корректности поэлементного умножения матрицы со скаляром
     */
    @Test
    public void multiplyDivideScalar() {
        Matrix m = new Matrix(testData);
        double multiplier = new Random().nextInt();
        Matrix m2 = m.mulScalar(multiplier);
        double delta = 0;
        for (int i = 0; i < testData.length; i++)
            for (int j = 0; j < testData[0].length; j++)
                assertEquals(m2.getElement(i, j), testData[i][j] * multiplier, delta);
        Matrix m1 = m2.divScalar(multiplier);
        assertArrayEquals(m.getDataClone(), m1.getDataClone());
    }

    /**
     * Проверка корректности сравнения матриц
     */
    @Test
    public void equals() {
        Matrix m = new Matrix(testData);
        assertFalse(m.equals(new Object()));
        assertFalse(m.equals(new Matrix(testDataTransposed)));
        testData[0][0]++;
        assertFalse(m.equals(new Matrix(testData)));
        testData[0][0]--;
        assertTrue(m.equals(new Matrix(testData)));
    }

    /**
     * Проверка корректности транспонирования
     */
    @Test
    public void transpose() {
        Matrix m = new Matrix(testData);
        Matrix mT = m.transpose();
        assertArrayEquals(mT.getDataClone(), testDataTransposed);
    }

    @Test
    public void get() {

    }

}
