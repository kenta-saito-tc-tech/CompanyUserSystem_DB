package com.example.companyusersystem_db;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {
    @FXML
    private Button stepBtn;

    @FXML
    private Button nextButton; // 次へボタン
    @FXML
    private Button prevButton; // 戻るボタン
    private int currentPage = 0;
    private int itemsPerPage = 50; // 1ページあたりの表示件数

    private ObservableList<Person> data;
    /**
     * ユーザー追加
     */
    @FXML
    private ComboBox addCompany;
    @FXML
    private TextArea addId;
    @FXML
    private TextArea addName;
    @FXML
    private TextArea addScore;
    @FXML
    private Button addUser;
    /**
     * ユーザー編集
     */
    @FXML
    private ComboBox changeCompany;
    @FXML
    private TextArea changeId;
    @FXML
    private TextArea changeName;
    @FXML
    private TextArea changeScore;
    @FXML
    private Button deleteUser;
    @FXML
    private Button changeUser;
    /**
     * 一覧表示
     */
    @FXML
    private TableView allView;
    @FXML
    private TableColumn idView;
    @FXML
    private TableColumn companyView;
    @FXML
    private TableColumn nameView;
    @FXML
    private TableColumn scoreView;

    UserService us;
    CompanyService cs;
    @FXML
    public void initialize() {
        //インスタンス
        UserService userService = new UserService();
        this.us = userService;
        CompanyService companyService = new CompanyService();
        this.cs = companyService;

        //TableViewの設定
        companyView.setCellValueFactory(new PropertyValueFactory<Person, String>("company"));
        idView.setCellValueFactory(new PropertyValueFactory<Person, Integer>("id"));
        nameView.setCellValueFactory(new PropertyValueFactory<Person, String>("name"));
        scoreView.setCellValueFactory(new PropertyValueFactory<Person, Integer>("score"));

        //ComboBoxの初期設定
        for(int i = 0; i < cs.count(); i++ ){
            addCompany.getItems().add(cs.findAll().get(i).name());
        }
        changeCompany.getItems().addAll(addCompany.getItems());


        //tableViewの初期表示
//        for(int i = 0; i < us.count(); i++ ){
//            allView.getItems().add(new Person(us.findAllTable(), i));
//        }

        updateTableView();

        // ページング操作用のボタンを設定する
        nextButton.setOnAction(event -> nextPage());
        prevButton.setOnAction(event -> previousPage());

        //追加ボタンクリック時処理
        addUser.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Person ps = new Person(addCompany.getValue().toString(),
                        addName.getText().toString(),
                        Integer.parseInt(addScore.getText()));
                //データのインサート
                us.insert(User.changeToUserNoId(ps));

                updateTableView();
            }
        });

        // TableViewのセルがクリックされたときの処理
        allView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) { // クリック回数が1回の場合
                Person selectedPerson = (Person) allView.getSelectionModel().getSelectedItem();
                if (selectedPerson != null) {
                    changeCompany.setValue(selectedPerson.companyProperty().getValue());
                    changeId.setText(String.valueOf(selectedPerson.idProperty().getValue()));
                    changeName.setText(selectedPerson.nameProperty().getValue());
                    changeScore.setText(selectedPerson.scoreProperty().getValue().toString());

                    //IDのtextViewを不変に設定
                    changeId.setEditable(false);

                    //削除ボタンクリック時処理
                    deleteUser.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            allView.getItems().remove(selectedPerson);
                            System.out.println(us.delete(selectedPerson.idProperty()));

                            updateTableView();
                        }
                    });

                    //更新ボタンクリック時処理
                    changeUser.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            // 選択された行の値を変更する
                            selectedPerson.setCompany(changeCompany.getValue().toString());
                            selectedPerson.setName(changeName.getText());
                            selectedPerson.setScore(Integer.parseInt(changeScore.getText()));
                            //データベースの更新
                            System.out.println(us.update(User.changeToUser(selectedPerson)));

                            updateTableView();
                        }
                    });
                }
            }
        });
    }

    /**
     * 次ページに移動
     * @param event
     */
    @FXML
    public void stepToCompany(ActionEvent event){
        Parent root = null;
        try {
            // FXMLファイルをロードして新しいSceneを作成する
            root = FXMLLoader.load(getClass().getResource("company-view.fxml"));
            Scene scene = new Scene(root);

            // Stageを取得し、新しいSceneをセットする
            Stage stage = (Stage) stepBtn.getScene().getWindow(); // 現在のStageを取得
            stage.setScene(scene); // 新しいSceneをセットする
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * TableViewの設定
     */
    private void updateTableView() {
        //tableViewの初期表示
        data = FXCollections.observableArrayList();
        for (int i = 0; i < us.count(); i++) {
            data.add(new Person(us.findAllTable(), i));
        }

        allView.setItems(data);

        // 表示すべきデータの範囲を計算する
        int startIndex = currentPage * itemsPerPage;
        int endIndex = Math.min(startIndex + itemsPerPage, us.findAllTable().size());

        // TableViewに表示するデータをセットする
        allView.setItems(FXCollections.observableArrayList(data.subList(startIndex, endIndex)));

        // ボタンの有効/無効状態を更新する
        nextButton.setDisable(endIndex >= data.size());
        prevButton.setDisable(currentPage == 0);
    }

    private void nextPage() {
        currentPage++;
        updateTableView();
    }

    private void previousPage() {
        currentPage--;
        updateTableView();
    }
}