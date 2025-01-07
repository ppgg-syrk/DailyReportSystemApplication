package com.techacademy.repository;

import com.techacademy.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Integer> {

    /** 
     * 論理削除されていない全日報を取得
     * @return 削除フラグがfalseの日報リスト
     */
    List<Report> findByDeleteFlgFalse();

    /** 
     * 指定された社員番号の論理削除されていない日報を取得
     * @param employeeCode 従業員コード
     * @return 該当の日報リスト
     */
    List<Report> findByEmployeeCodeAndDeleteFlgFalse(String employeeCode);
}
