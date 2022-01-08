package banking;

/**
 * класс работает с алгоритмом луна
 */
public class Moon {

    /**
     * метод генерирует контрольное число алгоритмом 'луна'
     *
     * @param num это число из которого будет вычисляться контрольное число
     * @return вычисленное контрольное число
     */
    public int generator(String num) {
        int resNum = Math.abs(foundNumber(num) % 10 - 10);
        return resNum != 10 ? resNum : 0;
    }

    /**
     * метод проверяет, соответствует ли число алгоритму 'луна'
     *
     * @param num проверяемое число
     * @return соответствует или нет
     */
    public boolean checkNum(String num) {
        return foundNumber(num) % 10 == 0;
    }

    /**
     * алгоритм поиска контрольного числа, используется в
     * методе проверки на алгоритм 'луна' и в методе генерации числа
     *
     * @param num это число из которого будет вычисляться контрольное число
     * @return вычисленное контрольное число
     */
    public int foundNumber(String num) {
        String[] arrNum = num.split("");
        int result = 0;
        for (int i = 0; i < arrNum.length; i++) {
            if (i % 2 == 0) {
                int xx = Integer.parseInt(arrNum[i]) * 2;
                if (xx > 9) {
                    xx = xx - 9;
                }
                result = result + xx;
            } else {
                result = result + Integer.parseInt(arrNum[i]);
            }
        }
        return result;
    }
}
