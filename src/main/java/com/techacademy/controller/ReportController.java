package com.techacademy.controller;

import com.techacademy.entity.Report;
import com.techacademy.service.ReportService;
import com.techacademy.service.UserDetail;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    /** 
     * 日報一覧画面を表示
     * @param userDetail ログイン中のユーザー情報
     * @param model モデルオブジェクト
     * @return 一覧テンプレート
     */
    @GetMapping
    public String list(@AuthenticationPrincipal UserDetail userDetail, Model model) {
        model.addAttribute("listSize", reportService.findReports(userDetail).size());
        model.addAttribute("reportList", reportService.findReports(userDetail));
        return "reports/list";
    }

    /** 
     * 日報詳細画面を表示
     * @param id 日報ID
     * @param model モデルオブジェクト
     * @return 詳細テンプレート
     */
    @GetMapping("/{id}")
    public String detail(@PathVariable Integer id, Model model) {
        model.addAttribute("report", reportService.findById(id));
        return "reports/detail";
    }

    /** 
     * 日報新規登録画面を表示
     * @param report 空の日報オブジェクト
     * @return 新規登録テンプレート
     */
    @GetMapping("/add")
    public String create(@ModelAttribute Report report) {
        return "reports/new";
    }

    /** 
     * 新規日報登録処理
     * @param report 登録する日報データ
     * @param res バリデーション結果
     * @param userDetail ログイン中のユーザー情報
     * @param model モデルオブジェクト
     * @return 一覧画面または登録画面
     */
    @PostMapping("/add")
    public String add(@Validated @ModelAttribute Report report, BindingResult res, @AuthenticationPrincipal UserDetail userDetail, Model model) {
        if (res.hasErrors()) {
            return create(report);
        }
        reportService.save(report, userDetail);
        return "redirect:/reports";
    }

    /** 
     * 日報更新画面を表示
     * @param id 更新対象の日報ID
     * @param model モデルオブジェクト
     * @return 更新テンプレート
     */
    @GetMapping("/{id}/update")
    public String edit(@PathVariable Integer id, Model model) {
        model.addAttribute("report", reportService.findById(id));
        return "reports/update";
    }

    /** 
     * 日報更新処理
     * @param id 更新対象の日報ID
     * @param report 更新後の日報データ
     * @param res バリデーション結果
     * @param model モデルオブジェクト
     * @return 一覧画面または更新画面
     */
    @PostMapping("/{id}/update")
    public String update(@PathVariable Integer id, @Validated @ModelAttribute Report report, BindingResult res, Model model) {
        if (res.hasErrors()) {
            model.addAttribute("report", report);
            return "reports/update";
        }
        reportService.update(id, report);
        return "redirect:/reports";
    }

    /** 
     * 日報削除処理
     * @param id 削除対象の日報ID
     * @return 一覧画面
     */
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Integer id) {
        reportService.delete(id);
        return "redirect:/reports";
    }
}
