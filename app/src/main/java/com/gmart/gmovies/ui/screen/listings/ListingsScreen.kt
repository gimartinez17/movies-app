package com.gmart.gmovies.ui.screen.listings

import AppBarState
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gmart.domain.model.Detail
import com.gmart.domain.model.ListType
import com.gmart.domain.model.ListType.Companion.mediaType
import com.gmart.domain.model.MediaType
import com.gmart.domain.model.UserList
import com.gmart.gmovies.R
import com.gmart.gmovies.navigation.MainBarRoutes
import com.gmart.gmovies.ui.base.SIDE_EFFECTS_KEY
import com.gmart.gmovies.ui.composable.AppDialog
import com.gmart.gmovies.ui.composable.AppError
import com.gmart.gmovies.ui.composable.ErrorSnackBarMessage
import com.gmart.gmovies.ui.composable.Loading
import com.gmart.gmovies.ui.screen.auth.composables.MustSignedInView
import com.gmart.gmovies.ui.screen.listings.composables.CreateListContent
import com.gmart.gmovies.ui.screen.listings.composables.EmptyListMessage
import com.gmart.gmovies.ui.screen.listings.composables.ListingDropDown
import com.gmart.gmovies.ui.screen.listings.composables.ListingResult

@Composable
fun ListingsScreen(
    paddingValues: PaddingValues = PaddingValues(),
    onComposing: (AppBarState) -> Unit = {},
    scaffoldState: SnackbarHostState,
    onLogInClick: () -> Unit = {},
    onSettingClick: () -> Unit = {},
    onMediaClick: (Int, MediaType) -> Unit = { _, _ -> },
    viewModel: ListingsViewModel = hiltViewModel(),
) {
    val viewState by viewModel.viewState.collectAsStateWithLifecycle()
    val selectedIndex by viewModel.selectedIndex.collectAsStateWithLifecycle()
    val listNames by viewModel.listNames.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()

    LaunchedEffect(SIDE_EFFECTS_KEY) { viewModel.setEvent(ListingEvent.GetListings) }

    if (viewState.isLoading) Loading(Modifier.fillMaxSize())

    if (viewState.data != null && viewState.listType != null) ListingContent(
        listNames = listNames,
        selectedIndex = selectedIndex,
        data = viewState.data,
        listType = viewState.listType,
        paddingValues = paddingValues,
        onComposing = onComposing,
        onMediaClick = onMediaClick,
        onListClick = { viewModel.setEvent(ListingEvent.SelectList(it)) },
        onMediaDelete = { id, mediaType ->
            viewModel.setEvent(ListingEvent.RemoveMedia(listNames[selectedIndex].id, id, mediaType))
        },
        onDeleteList = {
            viewModel.setEvent(ListingEvent.DeleteList(listNames[selectedIndex].id))
        },
        onCreateList = { name, description, isPublic, sortBy ->
            viewModel.setEvent(ListingEvent.CreateList(name, description, isPublic, sortBy))
        },
        onEditList = { name, description, isPublic, sortBy ->
            val listId = listNames[selectedIndex].id
            viewModel.setEvent(ListingEvent.EditList(listId, name, description, isPublic, sortBy))
        }
    )

    if (viewState.showMustBeSignedIn) MustSignedInView(
        paddingValues = paddingValues,
        onComposing = onComposing,
        onLoginClick = onLogInClick,
        onSettingClick = onSettingClick,
    )

    if (viewState.errorMessage != null)
        AppError(onRetry = { viewModel.setEvent(ListingEvent.GetListings) })

    if (viewState.snackBarMessage != null)
        ErrorSnackBarMessage(
            scaffoldState = scaffoldState,
            message = viewState.snackBarMessage ?: "",
            scope = scope,
        )
}

@Composable
private fun ListingContent(
    listNames: List<UserList>,
    selectedIndex: Int,
    data: List<Detail>?,
    listType: ListType?,
    onListClick: (Int) -> Unit,
    onMediaClick: (Int, MediaType) -> Unit = { _, _ -> },
    onMediaDelete: (Int, MediaType) -> Unit = { _, _ -> },
    onDeleteList: () -> Unit = { },
    onCreateList: (String, String, Boolean, String) -> Unit = { _, _, _, _ -> },
    onEditList: (String, String, Boolean, String) -> Unit = { _, _, _, _ -> },
    paddingValues: PaddingValues = PaddingValues(),
    onComposing: (AppBarState) -> Unit,
) {
    var showAddListModal by remember { mutableStateOf(false) }
    var editList by remember { mutableStateOf(false) }
    var showDeleteConfirmationModal by remember { mutableStateOf(false) }

    LaunchedEffect(SIDE_EFFECTS_KEY) {
        onComposing(
            AppBarState(
                key = MainBarRoutes.Lists.route, showBottomBar = true, showLogo = true,
            )
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = paddingValues.calculateTopPadding(),
                    bottom = paddingValues.calculateBottomPadding(),
                    start = 16.dp,
                    end = 16.dp
                )
        ) {
            ListingDropDown(
                modifier = Modifier.fillMaxWidth(),
                listNames = listNames,
                selectIndex = selectedIndex,
                onListClick = onListClick,
                onAddListClick = { showAddListModal = true },
            )
            if (data.isNullOrEmpty())
                EmptyListMessage(message = getEmptyMessage(listType))
            else
                ListingResult(
                    data = data,
                    mediaType = listType?.mediaType(),
                    onMediaClick = onMediaClick,
                    onMediaDelete = onMediaDelete
                )
        }
        if (listType == ListType.USER) ConfigList(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(
                    bottom = paddingValues.calculateBottomPadding() + 16.dp,
                    end = 16.dp
                ),
            onEditClick = {
                showAddListModal = true
                editList = true
            },
            onDeleteClick = { showDeleteConfirmationModal = true }
        )
        if (showAddListModal) {
            CreateListDialog(
                editable = editList,
                listData = if (editList) listNames[selectedIndex] else null,
                onDismiss = {
                    showAddListModal = false
                    editList = false
                },
                onConfirm = { name, description, isPublic, sortBy ->
                    showAddListModal = false
                    if (editList) onEditList(name, description, isPublic, sortBy)
                    else onCreateList(name, description, isPublic, sortBy)
                    editList = false
                },
            )
        }
        if (showDeleteConfirmationModal) {
            AppDialog(
                title = stringResource(id = R.string.delete_list),
                body = stringResource(id = R.string.delete_list_body),
                dismissText = stringResource(id = R.string.language_restart_cancel),
                confirmText = stringResource(id = R.string.confirm),
                onDismiss = { showDeleteConfirmationModal = false },
                onConfirm = {
                    showDeleteConfirmationModal = false
                    onDeleteList()
                },
            )
        }

    }
}

@Composable
private fun CreateListDialog(
    editable: Boolean = false,
    listData: UserList? = null,
    onDismiss: () -> Unit,
    onConfirm: (String, String, Boolean, String) -> Unit,
) {
    var name by remember {
        mutableStateOf(if (editable) (listData?.name ?: "") else "")
    }
    var description by remember {
        mutableStateOf(if (editable) (listData?.description ?: "") else "")
    }
    var isPublic by remember {
        mutableStateOf(if (editable) (listData?.public ?: false) else false)
    }
    var sortBy by remember {
        val sortOption = getSortNameByInt(listData?.sortBy) ?: sortingOptions.first()
        val default = if (editable) sortOption else sortingOptions.first()
        mutableStateOf(default)
    }
    AppDialog(
        title = stringResource(id = if (editable) R.string.edit_list else R.string.create_list),
        body = {
            CreateListContent(
                name = name,
                onNameChange = { name = it },
                description = description,
                onDescriptionChange = { description = it },
                isPublic = isPublic,
                onPublicChange = { isPublic = it },
                sortBy = sortBy,
                onSortByChange = { sortBy = it }
            )
        },
        dismissText = stringResource(id = R.string.language_restart_cancel),
        confirmText = stringResource(id = if (editable) R.string.edit_list else R.string.create_list),
        onDismiss = onDismiss,
        onConfirm = { onConfirm(name, description, isPublic, sortBy) },
        dismissOnClickOutside = false,
    )
}

@Composable
fun ConfigList(
    modifier: Modifier = Modifier,
    onEditClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {},
) {
    var showOptions by remember { mutableStateOf(false) }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.End,
    ) {
        AnimatedVisibility(
            visible = showOptions,
            enter = fadeIn(animationSpec = tween(1200)),
            exit = fadeOut(animationSpec = tween(600))
        ) {
            Column {
                SmallFloatingActionButton(
                    modifier = Modifier,
                    onClick = { onDeleteClick() },
                ) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = null
                    )
                }
                SmallFloatingActionButton(
                    modifier = Modifier.padding(top = 8.dp),
                    onClick = { onEditClick() },
                ) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = null
                    )
                }
            }
        }
        FloatingActionButton(
            modifier = Modifier.padding(top = 12.dp),
            onClick = { showOptions = !showOptions },
            containerColor = MaterialTheme.colorScheme.surface,
        ) {
            Icon(
                imageVector = if (showOptions) Icons.Filled.Close else Icons.Filled.Settings,
                contentDescription = null
            )
        }
    }
}

@Composable
private fun getEmptyMessage(listType: ListType?): String {
    return when (listType) {
        ListType.FAVOURITE_MOVIES -> stringResource(id = R.string.empty_favourite_movies)
        ListType.FAVOURITE_TV -> stringResource(id = R.string.empty_favourite_tv_shows)
        ListType.USER -> stringResource(id = R.string.empty_user_list)
        else -> stringResource(id = R.string.empty_user_list)
    }
}
