package com.kirk;

import java.util.Scanner;

import java.sql.SQLException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class AuxiliaryMethods {
    Scanner scanner = new Scanner(System.in);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    //вывод меню (для главной функции main)
    public void showMenu(){
        System.out.print("""
        \n-----Меню-----
        1) Добавить задачу
        2) Вывести список всех задач
        3) Завершить задачу
        4) Удалить задачу
        5) Редактировать задачу
        6) Отсортировать задачи по приоритету
        7) Отсортировать задачи по дате, до которой надо выполнить
        0) Выход
        Выберите действие:""");
    }

    //ввод значения для взаимодействия с консолью (для главной функции main)
    public int inputChoice(){
        Integer choice = 0;
        String entered_string = null;
        boolean valid_number = false;

        while(!valid_number){
            entered_string = scanner.nextLine();

            choice = parseInputChoice(entered_string);

            valid_number = checkChoice(choice);
        }
        
        return choice;
    }

    public Integer parseInputChoice(String input){
        Integer parseInt = null;
        
        try{
            parseInt = Integer.parseInt(input);
        } catch(NumberFormatException e){
            System.out.println("Ошибка! Неверный формат ввода. Введите число от 0 до 7.");
            System.out.println(e.getMessage());
        }
        
        return parseInt;
    }
    
    //проверка значения для взаимодействия с консолью (для функции inputChoice)
    public boolean checkChoice(Integer choice){
        if(choice == null){
            return false;
        }

        if(choice >= 0 && choice <= 7){
            return true;
        } else{
            System.out.println("Число не должно быть меньше 0 и больше 7!\nВведите число заново:");
            return false;
        }
    }

    //ввод приоритетности и проверка
    public int inputAndCheckPriority(){
        int priority = scanner.nextInt();
        scanner.nextLine();

        if(priority > 3){
            System.out.println("Приоритет не может быть выше 3!\nВведите приоритет заново: ");
            priority = inputAndCheckPriority();
        } else {
            return priority;
        }

        return priority;
    }
    
    //ввод даты и проверка формата 
    public LocalDate inputAndCheckDate(){
        String input = scanner.nextLine();
        LocalDate dueDate = null;
        
        try {
            dueDate = LocalDate.parse(input, formatter);
        } catch (DateTimeParseException e) {
            System.out.println("Неверный формат даты!\nВведите дату заново в формате 'дд.мм.гггг': ");
            dueDate = inputAndCheckDate();
        }
        
        return dueDate;
    }
    
    //ввод и проверка выполненности (для функции updateData в этом методе)
    public boolean inputAndCheckNewCompleted(){
        String strCompleted = scanner.nextLine();
        boolean result = false;

        if(strCompleted.length() > 1){
            System.out.println("Должен быть введен максимум один символ!\nСледуйте формату 'Y/n' и введите выполненность заново: ");
            return inputAndCheckNewCompleted();
        } else{
            if(strCompleted.equalsIgnoreCase("y")){
                result = true;
            } else if(strCompleted.equalsIgnoreCase("n")){
                result = false;
            } else{
                System.out.println("Неправильный формат ввода. Пожалуйста, введи выполненность заново в формате 'Y/n': ");
                return inputAndCheckNewCompleted();
            }
        }
        
        return result;
    }

    //проверка на изменения строк в базе
    public boolean checkChanges(java.sql.PreparedStatement stmt, String massage){
        boolean empty = false;

        try {
            int rows = stmt.executeUpdate();

            if(rows > 0){
                empty = true;
                System.out.println(massage);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return empty;
    }

    //ввод данных (для функции addTask)
    public Task inputData(){
        System.out.println("Введите название задачи: ");
        String tit = scanner.nextLine();

        System.out.println("Введите полное описание задачи: ");
        String des = scanner.nextLine();

        System.out.println("Введите дату, до которой надо выполнить задание (дд.мм.гггг): ");
        LocalDate dueD = inputAndCheckDate();

        System.out.println("Введите приоритет задачи \n(1-высший, 2-средний, 3-низший): ");
        int prior = inputAndCheckPriority();

        boolean compl = false;

        return new Task(tit, des, dueD, prior, compl);
    }

    //обновление данных (для функции editTask)
    public Task updateData(String title){
        System.out.println("Введите новое описание задачи: ");
        String newDes = scanner.nextLine();

        System.out.println("Введите новую дату(дд.мм.гггг): ");
        LocalDate newDate = inputAndCheckDate();

        System.out.println("Введите новую приоритетность: ");
        int newPrior = inputAndCheckPriority();

        System.out.println("Задача выполнена?(y/n): ");
        boolean newCompleted = inputAndCheckNewCompleted();

        return new Task(title, newDes, newDate, newPrior, newCompleted);
    }

    //получение данных с базы (для функций showTask, sortByPriority и sortByDate)
    public Task getData(java.sql.ResultSet res){
        String title = null;
        String description = null;
        LocalDate dueDate = null;
        int priority = 0;
        boolean completed = false;

        try {
            title = res.getString("title");
            description = res.getString("description");
            dueDate = res.getDate("dueDate").toLocalDate();
            priority = res.getInt("priority");
            completed = res.getBoolean("completed");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new Task(title, description , dueDate, priority, completed);
    }
}
