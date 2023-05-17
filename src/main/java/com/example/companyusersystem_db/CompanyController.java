package com.example.companyusersystem_db;

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

public class CompanyController {
    @FXML
    private Button backBtn;

    /**
     * 会社追加
     */
    @FXML
    private TextArea addId;
    @FXML
    private TextArea addName;
    @FXML
    private Button addCompany;
    /**
     * 会社編集
     */
    @FXML
    private TextArea changeId;
    @FXML
    private TextArea changeName;
    @FXML
    private Button deleteCompany;
    @FXML
    private Button changeCompany;
    /**
     * 一覧表示
     */
    @FXML
    private TableView allView;
    @FXML
    private TableColumn idView;
    @FXML
    private TableColumn companyView;

    CompanyService cs;
    @FXML
    public void initialize() {
        //インスタンス
        CompanyService companyService = new CompanyService();
        this.cs = companyService;

        //TableViewの設定
        idView.setCellValueFactory(new PropertyValueFactory<Person, Integer>("id"));
        companyView.setCellValueFactory(new PropertyValueFactory<Person, String>("name"));

        //tableViewの初期表示
        for(int i = 0; i < cs.count(); i++ ){
            allView.getItems().add(new WorkPlace(cs.findAll(), i));
        }

        //追加ボタンクリック時処理
        addCompany.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                WorkPlace ws = new WorkPlace(addName.getText().toString());
                //データのインサート
                cs.insert(Company.changeToCompanyNoId(ws));
                //TableView更新
                allView.getItems().clear();
                for(int i = 0; i < cs.count(); i++ ){
                    allView.getItems().add(new WorkPlace(cs.findAll(), i));
                }
            }
        });

        // TableViewのセルがクリックされたときの処理
        allView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) { // クリック回数が1回の場合
                WorkPlace selectedWorkPlace = (WorkPlace) allView.getSelectionModel().getSelectedItem();
                if (selectedWorkPlace != null) {
                    changeId.setText(String.valueOf(selectedWorkPlace.idProperty().getValue()));
                    changeName.setText(selectedWorkPlace.nameProperty().getValue());

                    //IDのtextViewを不変に設定
                    changeId.setEditable(false);

                    //削除ボタンクリック時処理
                    deleteCompany.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            allView.getItems().remove(selectedWorkPlace);
                            System.out.println(cs.delete(selectedWorkPlace.idProperty().intValue()));
                            //TableView更新
                            allView.getItems().clear();
                            for(int i = 0; i < cs.count(); i++ ){
                                allView.getItems().add(new WorkPlace(cs.findAll(), i));
                            }
                        }
                    });

                    //更新ボタンクリック時処理
                    changeCompany.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            // 選択された行の値を変更する
                            selectedWorkPlace.setId(Integer.parseInt(changeId.getText()));
                            selectedWorkPlace.setName(changeName.getText());
                            //データベースの更新
                            System.out.println(cs.update(Company.changeToCompany(selectedWorkPlace)));
                            //TableView更新
                            allView.getItems().clear();
                            for(int i = 0; i < cs.count(); i++ ){
                                allView.getItems().add(new WorkPlace(cs.findAll(), i));
                            }
                        }
                    });
                }
            }
        });
    }

    @FXML
    public void backToUser(ActionEvent event){
        Parent root = null;
        try {
            // FXMLファイルをロードして新しいSceneを作成する
            root = FXMLLoader.load(getClass().getResource("Main-View.fxml"));
            Scene scene = new Scene(root);

            // Stageを取得し、新しいSceneをセットする
            Stage stage = (Stage) backBtn.getScene().getWindow(); // 現在のStageを取得
            stage.setScene(scene); // 新しいSceneをセットする
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

