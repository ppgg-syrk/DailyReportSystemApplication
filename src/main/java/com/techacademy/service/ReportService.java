package com.techacademy.service;

import com.techacademy.constants.ErrorKinds;
import com.techacademy.entity.Report;
import com.techacademy.repository.ReportRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReportService {

    private final ReportRepository reportRepository;

    public ReportService(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    /** 
     * 全日報の取得（論理削除されていないデータのみ）
     * @return 日報リスト
     */
    public List<Report> findAll() {
        return reportRepository.findByDeleteFlgFalse();
    }

    /** 
     * ログインユーザーに応じた日報データを取得
     * 管理者の場合は全データ、一般ユーザーの場合は自分のデータのみ
     * @param userDetail ログイン中のユーザー情報
     * @return 日報リスト
     */
    public List<Report> findReports(UserDetail userDetail) {
        if (userDetail.isAdmin()) {
            return findAll();
        } else {
            return reportRepository.findByEmployeeCodeAndDeleteFlgFalse(userDetail.getEmployee().getCode());
        }
    }

    /** 
     * 日報をIDで取得
     * @param id 日報ID
     * @return 該当する日報
     */
    public Report findById(Integer id) {
        return reportRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("日報が見つかりません: ID = " + id));
    }

    /** 
     * 同一日付の日報が既に存在するかをチェック
     * @param reportDate 日報の日付
     * @param employeeCode 従業員コード
     * @return ErrorKinds.DATECHECK_ERROR or ErrorKinds.CHECK_OK
     */
    public ErrorKinds isDuplicateReport(LocalDate reportDate, String employeeCode) {
        boolean exists = reportRepository.existsByReportDateAndEmployeeCodeAndDeleteFlgFalse(reportDate, employeeCode);
        return exists ? ErrorKinds.DATECHECK_ERROR : ErrorKinds.CHECK_OK;
    }

    /** 
     * 新規日報を保存
     * @param report 新規登録する日報
     * @param userDetail ログイン中のユーザー情報
     */
    @Transactional
    public void save(Report report, UserDetail userDetail) {
        report.setEmployee(userDetail.getEmployee());
        report.setDeleteFlg(false);
        LocalDateTime now = LocalDateTime.now();
        report.setCreatedAt(now);
        report.setUpdatedAt(now);
        reportRepository.save(report);
    }

    /** 
     * 既存日報を更新
     * @param id 更新対象の日報ID
     * @param updatedReport 更新後の日報データ
     */
    @Transactional
    public void update(Integer id, Report updatedReport) {
        Report existingReport = findById(id);
        existingReport.setReportDate(updatedReport.getReportDate());
        existingReport.setTitle(updatedReport.getTitle());
        existingReport.setContent(updatedReport.getContent());
        existingReport.setUpdatedAt(LocalDateTime.now());
        reportRepository.save(existingReport);
    }

    /** 
     * 日報を論理削除
     * @param id 削除対象の日報ID
     */
    @Transactional
    public void delete(Integer id) {
        Report report = findById(id);
        report.setDeleteFlg(true);
        report.setUpdatedAt(LocalDateTime.now());
        reportRepository.save(report);
    }
}
