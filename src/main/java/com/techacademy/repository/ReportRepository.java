package com.techacademy.repository;

import com.techacademy.entity.Employee;
import com.techacademy.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
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

    /** 
     * 指定された日付と社員番号の論理削除されていない日報を取得
     * @param reportDate 日報の日付
     * @param employeeCode 従業員コード
     * @return 該当の日報リスト
     */
    List<Report> findByReportDateAndEmployeeCodeAndDeleteFlgFalse(LocalDate reportDate, String employeeCode);
    
    /** 従業員に紐づく削除されていない日報を取得 */
    List<Report> findByEmployeeAndDeleteFlgFalse(Employee employee);
    
    /** 
     * 指定の日付と従業員コードの未削除の日報が存在するかチェック
     * @param reportDate 日付
     * @param employeeCode 従業員コード
     * @return 存在する場合は true、それ以外は false
     */
    boolean existsByReportDateAndEmployeeCodeAndDeleteFlgFalse(LocalDate reportDate, String employeeCode);
}
