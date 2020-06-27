package com.dj.template;

import com.dj.util.Page;
import javafx.collections.FXCollections;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableView;

public class TableWithPaginationAndSorting<T> {
    private Page<T> page;
    private TableView<T> tableView;
    private Pagination tableViewWithPaginationPane;
    
    /** getter **/

    public TableWithPaginationAndSorting(Page<T> page, TableView<T> tableView,Pagination pagination) {
        this.page = page;
        this.tableView = tableView;
        tableViewWithPaginationPane = pagination;
        tableViewWithPaginationPane.pageCountProperty().bindBidirectional(page.getTotalPage());
        updatePagination();
    }

    private void updatePagination() {
        tableViewWithPaginationPane.setPageFactory(pageIndex -> {
            tableView.setItems(FXCollections.observableList(page.getCurrentPageDataList(pageIndex)));
            return tableView;
        });
    }
}