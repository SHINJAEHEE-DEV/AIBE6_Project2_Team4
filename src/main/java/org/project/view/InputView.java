package org.project.view;

import java.util.Scanner;

public class InputView {
    private final Scanner scanner = new Scanner(System.in);

    public String inputString() {
        return scanner.nextLine();
    }

    public int inputInt() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1; // 잘못된 입력 처리를 위한 플래그
        }
    }
}