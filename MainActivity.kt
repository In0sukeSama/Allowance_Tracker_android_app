package com.example.allowancetracker2

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    private var balance = 0.0
    private var dailyAllowance = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Views
        val etMonthly = findViewById<EditText>(R.id.etMonthlyAllowance)
        val etAmount = findViewById<EditText>(R.id.etAmount)
        val etDescription = findViewById<EditText>(R.id.etDescription)

        val tvDaily = findViewById<TextView>(R.id.tvDailyAllowance)
        val tvBalance = findViewById<TextView>(R.id.tvBalance)

        val btnSaveAllowance = findViewById<Button>(R.id.btnSaveAllowance)
        val btnEditAllowance = findViewById<Button>(R.id.btnEditAllowance)
        val btnAddDaily = findViewById<Button>(R.id.btnAddDaily)
        val btnSpend = findViewById<Button>(R.id.btnSpend)
        val btnAddExtra = findViewById<Button>(R.id.btnAddExtra)
        val btnResetBalance = findViewById<Button>(R.id.btnResetBalance)

        // SharedPreferences
        val prefs = getSharedPreferences("AllowancePrefs", Context.MODE_PRIVATE)

        // Load saved monthly allowance
        val savedMonthly = prefs.getFloat("monthly_allowance", -1f)
        if (savedMonthly != -1f) {
            etMonthly.setText(savedMonthly.toString())
            etMonthly.isEnabled = false
            dailyAllowance = savedMonthly.toDouble() / 30
            tvDaily.text = "Daily Allowance: ₹%.2f".format(dailyAllowance)
        }

        // Load saved balance
        balance = prefs.getFloat("current_balance", 0f).toDouble()
        tvBalance.text = "Balance: ₹%.2f".format(balance)

        // Save Monthly Allowance
        btnSaveAllowance.setOnClickListener {
            val monthly = etMonthly.text.toString().toDoubleOrNull() ?: return@setOnClickListener
            prefs.edit().putFloat("monthly_allowance", monthly.toFloat()).apply()

            dailyAllowance = monthly / 30
            tvDaily.text = "Daily Allowance: ₹%.2f".format(dailyAllowance)
            etMonthly.isEnabled = false
        }

        // Edit Monthly Allowance
        btnEditAllowance.setOnClickListener {
            etMonthly.isEnabled = true
        }

        // Add Daily Allowance
        btnAddDaily.setOnClickListener {
            balance += dailyAllowance
            saveBalance(prefs)
            tvBalance.text = "Balance: ₹%.2f".format(balance)
        }

        // Spend Money
        btnSpend.setOnClickListener {
            val amount = etAmount.text.toString().toDoubleOrNull() ?: return@setOnClickListener
            balance -= amount
            saveBalance(prefs)
            tvBalance.text = "Balance: ₹%.2f".format(balance)
            etAmount.text.clear()
            etDescription.text.clear()
        }

        // Add Extra Money
        btnAddExtra.setOnClickListener {
            val amount = etAmount.text.toString().toDoubleOrNull() ?: return@setOnClickListener
            balance += amount
            saveBalance(prefs)
            tvBalance.text = "Balance: ₹%.2f".format(balance)
            etAmount.text.clear()
            etDescription.text.clear()
        }

        // Reset Balance
        btnResetBalance.setOnClickListener {
            balance = 0.0
            saveBalance(prefs)
            tvBalance.text = "Balance: ₹0.00"
        }
    }

    private fun saveBalance(prefs: android.content.SharedPreferences) {
        prefs.edit().putFloat("current_balance", balance.toFloat()).apply()
    }
}
