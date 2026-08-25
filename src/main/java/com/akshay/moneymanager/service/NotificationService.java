package com.akshay.moneymanager.service;

import com.akshay.moneymanager.dto.ExpenseDTO;
import com.akshay.moneymanager.entity.ProfileEntity;
import com.akshay.moneymanager.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final ProfileRepository profileRepository;
    private final EmailService emailService;
    private final ExpenseService expenseService;

    @Value("${money.manager.frontend.url}")
    private String frontendUrl;

    @Scheduled(cron ="0 0 22 * * *",zone = "IST")
    public void sendDailyIncomeExpenseReminder(){
        log.info("Job started: sendDailyIncomeExpenseReminder()");

        List<ProfileEntity> profiles = profileRepository.findByIsActiveTrue();

        for (ProfileEntity profile : profiles) {

            String body = """
            <html>
            <body style="font-family: Arial, sans-serif; line-height: 1.6;">
                <p>Hi %s,</p>

                <p>
                    This is a friendly reminder to add your <b>income</b> and
                    <b>expenses</b> for today in <b>Money Manager</b>.
                </p>

                <p>
                    <a href="%s"
                       style="
                            display: inline-block;
                            padding: 10px 20px;
                            background-color: #28a745;
                            color: white;
                            text-decoration: none;
                            border-radius: 5px;">
                        Open Money Manager
                    </a>
                </p>

                <p>
                    Thank you for staying on top of your finances!
                </p>

                <p>
                    Best regards,<br>
                    <b>Money Manager Team</b>
                </p>
            </body>
            </html>
            """.formatted(profile.getFullName(), frontendUrl);

            emailService.sendEmail(
                    profile.getEmail(),
                    "Daily Reminder: Add Your Income & Expenses",
                    body
            );
        }

        log.info("Job completed: sendDailyIncomeExpenseReminder()");
    }

    @Scheduled(cron = "0 0 23 * * *", zone = "IST")
    public void sendDailyExpenseSummary() {

        log.info("Job started: sendDailyExpenseSummary()");

        List<ProfileEntity> profiles = profileRepository.findByIsActiveTrue();

        for (ProfileEntity profile : profiles) {

            List<ExpenseDTO> todaysExpenses =
                    expenseService.getExpensesForUserOnDate(profile.getId(), LocalDate.now());

            if (todaysExpenses.isEmpty()) {
                continue;
            }

            BigDecimal totalExpense = BigDecimal.ZERO;

            StringBuilder table = new StringBuilder();

            table.append("""
                <table style="width:100%; border-collapse:collapse; font-family:Arial,sans-serif;">
                    <thead>
                        <tr style="background-color:#4CAF50; color:white;">
                            <th style="border:1px solid #ddd; padding:8px;">#</th>
                            <th style="border:1px solid #ddd; padding:8px;">Expense</th>
                            <th style="border:1px solid #ddd; padding:8px;">Amount</th>
                            <th style="border:1px solid #ddd; padding:8px;">Category</th>
                        </tr>
                    </thead>
                    <tbody>
                """);

            int index = 1;

            for (ExpenseDTO expense : todaysExpenses) {

                totalExpense = totalExpense.add(expense.getAmount());

                table.append("<tr>");

                table.append("<td style='border:1px solid #ddd;padding:8px;'>")
                        .append(index++)
                        .append("</td>");

                table.append("<td style='border:1px solid #ddd;padding:8px;'>")
                        .append(expense.getName())
                        .append("</td>");

                table.append("<td style='border:1px solid #ddd;padding:8px;text-align:right;'>₹")
                        .append(expense.getAmount().setScale(2, RoundingMode.HALF_UP))
                        .append("</td>");

                table.append("<td style='border:1px solid #ddd;padding:8px;'>")
                        .append(expense.getCategoryId() != null
                                ? expense.getCategoryId()
                                : "N/A")
                        .append("</td>");

                table.append("</tr>");
            }

            table.append("""
                    </tbody>
                </table>
                """);

            String body = """
                <html>
                <body style="font-family:Arial,sans-serif; line-height:1.6; color:#333;">

                    <h2 style="color:#4CAF50;">Daily Expense Summary</h2>

                    <p>Hi <strong>%s</strong>,</p>

                    <p>
                        Here is a summary of your expenses for
                        <strong>%s</strong>.
                    </p>

                    %s

                    <p style="margin-top:20px; font-size:16px;">
                        <strong>Total Expense:</strong>
                        <span style="color:#d32f2f;">₹%s</span>
                    </p>

                    <p>
                        Keep tracking your spending to build better financial habits.
                    </p>

                    <br>

                    <p>
                        Best regards,<br>
                        <strong>Money Manager Team</strong>
                    </p>

                </body>
                </html>
                """.formatted(
                    profile.getFullName(),
                    LocalDate.now(),
                    table,
                    totalExpense.setScale(2, RoundingMode.HALF_UP)
            );

            emailService.sendEmail(
                    profile.getEmail(),
                    "Your Daily Expense Summary",
                    body
            );

            log.info("Daily expense summary sent to {}", profile.getEmail());
        }

        log.info("Job completed: sendDailyExpenseSummary()");
    }
}
