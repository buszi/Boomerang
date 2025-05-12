package io.buszi.boomerang

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import io.buszi.boomerang.core.BoomerangStore
import io.buszi.boomerang.core.BoomerangStoreHost
import io.buszi.boomerang.core.boomerangOf
import io.buszi.boomerang.databinding.FragmentALayoutBinding
import io.buszi.boomerang.databinding.FragmentBLayoutBinding
import io.buszi.boomerang.databinding.FragmentCLayoutBinding
import io.buszi.boomerang.fragment.catchBoomerangWithLifecycleEvent
import io.buszi.boomerang.fragment.createOrRestoreDefaultBoomerangStore
import io.buszi.boomerang.fragment.findBoomerangStore
import io.buszi.boomerang.fragment.saveDefaultBoomerangStoreState

/**
 * This activity demonstrates the usage of Boomerang with Fragments.
 * It showcases four key scenarios:
 * 1. Test navigation result is passed correctly
 * 2. Test navigation result is persisted correctly across configuration changes
 * 3. Test value can be dropped
 * 4. Test value can be proxied by another catcher in between
 */
class FullFragmentPreviewActivity : AppCompatActivity(), BoomerangStoreHost {

    override var boomerangStore: BoomerangStore? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Create or restore the BoomerangStore from the saved instance state
        // This is essential for TEST CASE 2: Persisting navigation results across configuration changes
        createOrRestoreDefaultBoomerangStore(savedInstanceState)
        setContentView(R.layout.full_fragment_preview_activity_layout)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Save the BoomerangStore state to the outState bundle
        // This is essential for TEST CASE 2: Persisting navigation results across configuration changes
        saveDefaultBoomerangStoreState(outState)
    }
}

/**
 * Home screen fragment that demonstrates catching and displaying navigation results.
 * This fragment is the destination for navigation results.
 */
class FragmentA : Fragment(R.layout.fragment_a_layout) {
    // Using a property to store the current result for display
    private var currentResult: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TEST CASE 1: Test navigation result is passed correctly
        // Set up a BoomerangCatcher that will be triggered when the fragment starts
        catchBoomerangWithLifecycleEvent("fragment_a") { bundle ->
            // Extract the result from the bundle
            bundle.getString("result")?.let { result ->
                // Store the result for later use
                currentResult = result
                // Update the UI if the view is available
                updateResultText()
            }
            // Return true to indicate that the value was caught and should be removed from the store
            true
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Restore the result from savedInstanceState if available
        // This is part of TEST CASE 2: Persisting navigation results across configuration changes
        savedInstanceState?.getString("current_result")?.let {
            currentResult = it
            updateResultText()
        }

        with(FragmentALayoutBinding.bind(view)) {
            // Update the result text with the current value
            resultLabel.text = currentResult ?: "No result yet"

            // Navigate to the intermediate screen
            navigateButton.setOnClickListener {
                findNavController().navigate(R.id.navigateB)
            }

            // Clear the current result
            clearButton.setOnClickListener {
                currentResult = null
                resultLabel.text = "No result yet"
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Save the current result to the outState bundle
        // This is part of TEST CASE 2: Persisting navigation results across configuration changes
        currentResult?.let {
            outState.putString("current_result", it)
        }
    }

    private fun updateResultText() {
        view?.let {
            FragmentALayoutBinding.bind(it).resultLabel.text = currentResult ?: "No result yet"
        }
    }
}

/**
 * Intermediate screen fragment that demonstrates various Boomerang operations.
 * This fragment shows how to drop values and proxy values between screens.
 */
class FragmentB : Fragment(R.layout.fragment_b_layout) {
    // Variable to store the proxied/consumed value
    private var proxiedValue: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Restore the proxied value from savedInstanceState if available
        savedInstanceState?.getString("proxied_value")?.let {
            proxiedValue = it
            updateProxiedValueText(view)
        }

        with(FragmentBLayoutBinding.bind(view)) {
            // Update the proxied value text with the current value
            updateProxiedValueText(view)

            // TEST CASE 1: Continue navigation to result screen
            navigateButton.setOnClickListener {
                findNavController().navigate(R.id.navigateC)
            }

            // TEST CASE 3: Test value can be dropped
            dropValueButton.setOnClickListener {
                // Explicitly drop the value from the store
                findBoomerangStore().dropValue("fragment_a")
            }

            // TEST CASE 2: Test navigation result is persisted correctly across configuration changes
            recreateButton.setOnClickListener {
                // Recreate the activity to simulate a configuration change
                activity?.recreate()
            }

            // Button to clear the proxied value
            clearProxiedValueButton.setOnClickListener {
                proxiedValue = null
                updateProxiedValueText(view)
            }

            // TEST CASE 4: Test value can be proxied by another catcher in between
            // Use the dedicated button to demonstrate consuming a value
            consumeValueButton.setOnClickListener {
                // Immediately catch and consume the value
                findBoomerangStore().tryConsumeValue("fragment_a") { bundle ->
                    // Get the value for display or processing in this screen
                    val consumedValue = bundle.getString("result")
                    // Store the consumed value for display
                    proxiedValue = consumedValue
                    // Update the UI with the consumed value
                    updateProxiedValueText(view)
                    // Return true to remove the value so the home screen won't get it
                    true
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Save the proxied value to the outState bundle
        proxiedValue?.let {
            outState.putString("proxied_value", it)
        }
    }

    // Helper method to update the proxied value text
    private fun updateProxiedValueText(view: View) {
        FragmentBLayoutBinding.bind(view).proxiedValueLabel.text =
            "Proxied value: ${proxiedValue ?: "No value"}"
    }
}

/**
 * Result screen fragment that demonstrates storing navigation results.
 * This fragment is the source of navigation results.
 */
class FragmentC : Fragment(R.layout.fragment_c_layout) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(FragmentCLayoutBinding.bind(view)) {
            // TEST CASE 1: Test navigation result is passed correctly
            navigateBackButton.setOnClickListener {
                // Store a result value with the key "fragment_a"
                findBoomerangStore().storeValue("fragment_a", boomerangOf("result" to "Result from result screen"))
                // Navigate back
                findNavController().popBackStack()
            }
        }
    }
}
