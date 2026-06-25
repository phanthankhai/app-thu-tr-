package com.example.rent.presentation.rooms

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.rent.R
import com.example.rent.core.common.UiState
import com.example.rent.core.mock.SmartRentMockData
import com.example.rent.databinding.FragmentRoomsBinding
import com.example.rent.presentation.model.RoomUiModel

class RoomsFragment : Fragment() {
    private var _binding: FragmentRoomsBinding? = null
    private val binding get() = _binding!!
    private val roomsState: UiState<List<RoomUiModel>> = UiState.Success(SmartRentMockData.rooms)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentRoomsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addRoomButton.setOnClickListener {
            findNavController().navigate(R.id.action_rooms_to_add_room)
        }

        binding.emptyAddRoomButton.setOnClickListener {
            findNavController().navigate(R.id.action_rooms_to_add_room)
        }

        binding.room101Card.setOnClickListener {
            findNavController().navigate(R.id.action_rooms_to_room_detail)
        }

        binding.viewRoom101Button.setOnClickListener {
            findNavController().navigate(R.id.action_rooms_to_room_detail)
        }

        binding.room203Card.setOnClickListener {
            findNavController().navigate(R.id.action_rooms_to_room_detail)
        }

        binding.room305Card.setOnClickListener {
            findNavController().navigate(R.id.action_rooms_to_room_detail)
        }

        renderState(roomsState)
    }

    private fun renderState(state: UiState<List<RoomUiModel>>) {
        when (state) {
            UiState.Loading -> renderLoading()
            is UiState.Success -> {
                if (state.data.isEmpty()) renderEmpty() else renderContent(state.data)
            }
            is UiState.Empty -> renderEmpty()
            is UiState.Error -> renderError(state.message)
        }
    }

    private fun renderLoading() {
        setRoomCardsVisible(false)
        binding.roomsEmptyStateCard.visibility = View.GONE
    }

    private fun renderContent(rooms: List<RoomUiModel>) {
        val hasRooms = rooms.isNotEmpty()
        setRoomCardsVisible(hasRooms)
        binding.roomsEmptyStateCard.visibility = if (hasRooms) View.GONE else View.VISIBLE
    }

    private fun renderEmpty() {
        setRoomCardsVisible(false)
        binding.roomsEmptyStateCard.visibility = View.VISIBLE
    }

    private fun renderError(message: String) {
        setRoomCardsVisible(false)
        binding.roomsEmptyStateCard.visibility = View.GONE
    }

    private fun setRoomCardsVisible(isVisible: Boolean) {
        val visibility = if (isVisible) View.VISIBLE else View.GONE
        binding.room101Card.visibility = visibility
        binding.room203Card.visibility = visibility
        binding.room305Card.visibility = visibility
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
