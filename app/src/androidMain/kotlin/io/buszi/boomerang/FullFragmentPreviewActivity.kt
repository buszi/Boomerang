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
import io.buszi.boomerang.fragment.catchEventBoomerangWithLifecycleEvent
import io.buszi.boomerang.fragment.createOrRestoreDefaultBoomerangStore
import io.buszi.boomerang.fragment.findBoomerangStore
import io.buszi.boomerang.fragment.saveDefaultBoomerangStoreState
import io.buszi.boomerang.fragment.serialization.kotlinx.consumeSerializableWithLifecycleEvent
import io.buszi.boomerang.serialization.kotlinx.storeValue
import kotlinx.serialization.Serializable

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
 * A serializable data class used to demonstrate Kotlinx Serialization integration with Boomerang.
 * 
 * This class is used in the fragment navigation example to show how serializable objects
 * can be passed between fragments using Boomerang's serialization support.
 *
 * @property name The name of the item
 * @property age The age value associated with the item
 */
@Serializable
data class SerializableItem(val name: String, val age: Int)

/**
 * Home screen fragment that demonstrates catching and displaying navigation results.
 * This fragment is the destination for navigation results.
 */
class FragmentA : Fragment(R.layout.fragment_a_layout) {
    // Using a property to store the current result for display
    private var currentResult: String? = null
    // Using a property to track when an event is received
    private var eventReceived: Boolean = false
    // Using a property to store the serializable item
    private var serializableItem: SerializableItem? = null

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

        // TEST CASE 5: Test event handling
        // Set up an EventBoomerangCatcher that will be triggered when the fragment starts
        catchEventBoomerangWithLifecycleEvent("fragment_a_event") {
            // Update the state to indicate that the event was received
            eventReceived = true
            // Update the UI if the view is available
            updateEventStatus()
        }

        consumeSerializableWithLifecycleEvent<SerializableItem> { item ->
            // Store the serializable item for later use
            serializableItem = item
            // Update the UI if the view is available
            updateSerializableItemText()
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

        // Restore the event status from savedInstanceState if available
        if (savedInstanceState != null) {
            eventReceived = savedInstanceState.getBoolean("event_received", false)
            updateEventStatus()
            
            // Restore the serializable item if available
            savedInstanceState.getString("serializable_item")?.let { serializedItem ->
                try {
                    // This is a simplified approach - in a real app, you might want to use a proper serialization library
                    val parts = serializedItem.split(",")
                    if (parts.size == 2) {
                        val name = parts[0]
                        val age = parts[1].toIntOrNull() ?: 0
                        serializableItem = SerializableItem(name, age)
                        updateSerializableItemText()
                    }
                } catch (e: Exception) {
                    // Handle deserialization error
                }
            }
        }

        with(FragmentALayoutBinding.bind(view)) {
            // Update the result text with the current value
            resultLabel.text = currentResult ?: "No result yet"

            // Update the event status text
            eventStatusLabel.text = "Event status: ${if (eventReceived) "Event received" else "No event received"}"
            
            // Update the serializable item text
            serializableItemLabel.text = "Serializable item: ${serializableItem ?: "No item yet"}"

            // Navigate to the intermediate screen
            navigateButton.setOnClickListener {
                findNavController().navigate(R.id.navigateB)
            }

            // Clear the current result
            clearButton.setOnClickListener {
                currentResult = null
                resultLabel.text = "No result yet"
            }

            // Clear the event status
            clearEventButton.setOnClickListener {
                eventReceived = false
                updateEventStatus()
            }
            
            // Clear the serializable item
            clearSerializableButton.setOnClickListener {
                serializableItem = null
                updateSerializableItemText()
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

        // Save the event status to the outState bundle
        outState.putBoolean("event_received", eventReceived)
        
        // Save the serializable item to the outState bundle
        serializableItem?.let {
            // This is a simplified approach - in a real app, you might want to use a proper serialization library
            outState.putString("serializable_item", "${it.name},${it.age}")
        }
    }

    private fun updateResultText() {
        view?.let {
            FragmentALayoutBinding.bind(it).resultLabel.text = currentResult ?: "No result yet"
        }
    }

    private fun updateEventStatus() {
        view?.let {
            FragmentALayoutBinding.bind(it).eventStatusLabel.text = 
                "Event status: ${if (eventReceived) "Event received" else "No event received"}"
        }
    }
    
    private fun updateSerializableItemText() {
        view?.let {
            FragmentALayoutBinding.bind(it).serializableItemLabel.text = 
                "Serializable item: ${serializableItem ?: "No item yet"}"
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

            // TEST CASE 5: Test event handling
            triggerEventButton.setOnClickListener {
                // Store an event with the key "fragment_a_event"
                findBoomerangStore().storeEvent("fragment_a_event")
                // Navigate back
                findNavController().popBackStack()
            }
            
            // Store a serializable item and navigate back
            storeSerializableButton.setOnClickListener {
                // Create a serializable item
                val item = SerializableItem("Test Name", 25)
                // Store the serializable item using the extension function
                findBoomerangStore().storeValue(item)
                // Navigate back
                findNavController().popBackStack()
            }
        }
    }
}
