package com.example.snfcalculator

import android.health.connect.datatypes.units.Percentage
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.snfcalculator.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var inputValue1: Double? = 0.0
    private var inputValue2: Double? = null
    private var currentOperator: Operator? = null
    private var result: Double? = null
    private var equation: StringBuilder = StringBuilder().append(ZERO)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setListeners()
        setNightModeIndicator()
    }

    private fun setListeners(){
        for (button in getNumericButtons()){
            button.setOnClickListener{ onNumberClicked(button.text.toString())}
        }
        with(binding){
            buttonZero.setOnClickListener{onZeroClicked()}
            buttonDoubleZero.setOnClickListener { onDoubleZeroClicked() }
            buttonDecimalPoint.setOnClickListener { onDecimalPointClicked() }
            buttonAddition.setOnClickListener { onOperatorClicked(Operator.ADDITION)}
            buttonSubtraction.setOnClickListener { onOperatorClicked(Operator.SUBTRACTION)}
            buttonMultiplication.setOnClickListener { onOperatorClicked(Operator.MULTIPLICATION)}
            buttonDivision.setOnClickListener { onOperatorClicked(Operator.DIVISION)}
            buttonEquals.setOnClickListener { onEqualsClicked() }
            buttonAllClear.setOnClickListener { onAllClearClicked() }
            buttonPlusMinus.setOnClickListener { onPlusMinusClicked() }
            buttonPercentage.setOnClickListener { onPercentageClicked() }
            imageNightMode.setOnClickListener { toggleNightMode() }

        }
    }

    private fun toggleNightMode(){
        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        } else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
        recreate()
    }

    private fun setNightModeIndicator(){
        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
            binding.imageNightMode.setImageResource(R.drawable.ic_sun )
        } else{
            binding.imageNightMode.setImageResource(R.drawable.ic_moon )
        }
    }

    private fun onPercentageClicked(){
        if(inputValue2 == null){
            val percentage = getInputValue1() / 100
            inputValue1 = percentage
            equation.clear().append(percentage)
            updateInputOnDisplay()
        }
        else {
            var percentageOfValue1 = (getInputValue1() * getInputValue2()) / 100
            var percentageOfValue2 =  getInputValue2() / 100
            result = when (requireNotNull(currentOperator)){
                 Operator.ADDITION -> getInputValue1() + percentageOfValue1
                 Operator.SUBTRACTION -> getInputValue1() - percentageOfValue1
                Operator.MULTIPLICATION -> getInputValue1() * percentageOfValue2
                Operator.DIVISION -> getInputValue1() / percentageOfValue2
            }
            equation.clear().append(ZERO)
            updateResultOnDisplay(isPercentage = true)
            inputValue1 = result
            result = null
            inputValue2 = null
            currentOperator = null

        }
    }
    private fun onPlusMinusClicked(){
        if(equation.startsWith(MINUS)){
            equation.deleteCharAt(0)
        }else{
            equation.insert(0, MINUS)

        }
        setInput()
        updateInputOnDisplay()
    }

    private fun onAllClearClicked(){
        inputValue1 = 0.0
        inputValue2 = null
        currentOperator = null
        result = null
        equation.clear().append(ZERO)
        clearDisplay()
    }

    private fun onOperatorClicked(operator: Operator){
        onEqualsClicked()
        currentOperator = operator
    }

    private fun onEqualsClicked() {
        if(inputValue2 != null) {
            result = calculate()
            equation.clear().append(ZERO)
            updateResultOnDisplay()
            inputValue1 = result
            result = null
            inputValue2 = null
            currentOperator = null
        }
        else{
            equation.clear().append(ZERO)
        }
    }

    private fun calculate(): Double{
        return when (requireNotNull(currentOperator)){
            Operator.ADDITION -> getInputValue1() + getInputValue2()
            Operator.SUBTRACTION -> getInputValue1() - getInputValue2()
            Operator.MULTIPLICATION -> getInputValue1() * getInputValue2()
            Operator.DIVISION -> getInputValue1() / getInputValue2()
        }
    }

    private fun onDecimalPointClicked(){
        if(equation.contains(DECIMAL_POINT))return
        equation.append(DECIMAL_POINT)
        setInput()
        updateInputOnDisplay()
    }

    private fun onZeroClicked(){
        if(equation.startsWith(ZERO))return
        onNumberClicked(ZERO)
    }

    private fun onDoubleZeroClicked(){
        if(equation.startsWith(ZERO))return
        onNumberClicked(DOUBLE_ZERO)
    }

    private fun getNumericButtons() = with(binding){
        arrayOf(
            buttonOne,
            buttonTwo,
            buttonThree,
            buttonFour,
            buttonFive,
            buttonSix,
            buttonSeven,
            buttonEight,
            buttonNine
        )
    }


    private fun onNumberClicked(numberText: String){
        if(equation.startsWith(ZERO)){
            equation.deleteCharAt(0)
        }else if(equation.startsWith("$MINUS$ZERO")){
            equation.deleteCharAt(1)
        }
        equation.append(numberText)
        setInput()
        updateInputOnDisplay()
    }

    private fun setInput(){
        if (currentOperator == null){
            inputValue1 =equation.toString().toDouble()
        }else{
            inputValue2 = equation.toString().toDouble()
        }
    }

    private fun clearDisplay(){
        with(binding){
            textInput.text = getFormattedDisplayValue(Value = getInputValue1())
            textEquation.text = null
        }
    }

    private fun updateResultOnDisplay(isPercentage: Boolean = false){
        binding.textInput.text = getFormattedDisplayValue(Value = result)
        var input2Text = getFormattedDisplayValue(Value = getInputValue2())
        if (isPercentage) input2Text = "$input2Text${getString(R.string.percentage)}"
        binding.textEquation.text = String.format(
            "%s %s %s",
            getFormattedDisplayValue(Value = getInputValue1()),
            getOperatorSymbol(),
            input2Text
        )

    }

    private fun updateInputOnDisplay() {
        if(result == null) {
            binding.textEquation.text = null
        }
        binding.textInput.text = equation
    }

    private fun getInputValue1() = inputValue1 ?: 0.0
    private fun getInputValue2() = inputValue2 ?: 0.0


    private fun getOperatorSymbol(): String{
        return when(requireNotNull(currentOperator)){
            Operator.ADDITION -> getString(R.string.addition)
            Operator.SUBTRACTION -> getString(R.string.subtraction)
            Operator.MULTIPLICATION -> getString(R.string.multiplication)
            Operator.DIVISION -> getString(R.string.division)
        }
    }

    private fun getFormattedDisplayValue(Value: Double?): String? {
        val originalValue = Value ?: return null
        return if(originalValue %  1 ==  0.0){
            originalValue.toInt().toString()
        } else{
            originalValue.toString()
        }
    }

    enum class Operator {
        ADDITION, SUBTRACTION, MULTIPLICATION, DIVISION

    }

    private companion object {
        const val DECIMAL_POINT = "."
        const val ZERO = "0"
        const val DOUBLE_ZERO = "00"
        const val MINUS = "-"

    }
}