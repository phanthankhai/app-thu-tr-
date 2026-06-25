# SmartRent Frontend Phase Change Log

## Phase 1A - Frontend Foundation

### 1. Mục tiêu phase

Phase 1A chuẩn hóa nền frontend Android Native XML + Kotlin: bật ViewBinding, thêm Navigation Component/Lifecycle dependencies tối thiểu, tạo package foundation, tạo `NavHostFragment` và các Fragment placeholder cơ bản.

### 2. File đã tạo/sửa

- Đã sửa `app/build.gradle.kts` để bật `viewBinding = true` và thêm dependency Lifecycle + Navigation XML.
- Đã sửa `gradle/libs.versions.toml` để thêm alias dependency cần thiết.
- Đã sửa `MainActivity.kt` để dùng `ActivityMainBinding`.
- Đã sửa `activity_main.xml` để chứa `NavHostFragment`.
- Đã tạo `main_nav_graph.xml`.
- Đã tạo package foundation `core`, `data`, `domain`, `navigation`, `presentation`.
- Đã tạo placeholder Fragment: Auth, Home, Rooms, Tenants, Bills, Profile.

### 3. Build status

- `assembleDebug` build thành công khi dùng Android Studio JBR hợp lệ.

### 4. Kết luận

**READY_FOR_PHASE_1B**

## Phase 1B - XML Design System Foundation

### 1. Mục tiêu phase

Phase 1B chuẩn hóa nền Design System cho Android XML: color tokens, dimens tokens, typography styles, component styles, theme tokens và một số drawable nền tối thiểu. Phase này không dựng màn hình thật, không kết nối API, không thêm backend/business logic, không đổi `namespace`/`applicationId`, không dùng Compose và không dùng web tech.

### 2. File đã tạo/sửa

#### File đã sửa

- `app/src/main/res/values/colors.xml`
  - Thêm hệ màu `sr_*` theo `SMARTRENT_MOBILE_UI_GUIDE.md`.
  - Giữ token cũ từ Phase 1A để không phá compatibility.
- `app/src/main/res/values/dimens.xml`
  - Bổ sung spacing, radius, component size, elevation và text size tokens.
- `app/src/main/res/values/themes.xml`
  - Chuẩn hóa theme light dùng token SmartRent cho primary, secondary, surface, error, status/navigation bar.
- `app/src/main/res/values-night/themes.xml`
  - Đồng bộ token theme night ở mức an toàn, chưa làm dark mode đầy đủ.
- Placeholder layouts:
  - `app/src/main/res/layout/fragment_auth.xml`
  - `app/src/main/res/layout/fragment_home.xml`
  - `app/src/main/res/layout/fragment_rooms.xml`
  - `app/src/main/res/layout/fragment_tenants.xml`
  - `app/src/main/res/layout/fragment_bills.xml`
  - `app/src/main/res/layout/fragment_profile.xml`
  - Chuyển placeholder sang dùng `bg_app`, `space_16` và `TextAppearance.SmartRent.Body`.

#### File đã tạo mới

- `app/src/main/res/values/styles.xml`
  - Thêm typography styles và component styles reusable cho XML.
- `app/src/main/res/drawable/bg_app.xml`
  - Background app nền trung tính.
- `app/src/main/res/drawable/bg_card.xml`
  - Card background nền trắng, border mảnh, radius theo guide.
- `app/src/main/res/drawable/bg_chip.xml`
  - Chip/status background nền nhạt.
- `app/src/main/res/drawable/bg_input.xml`
  - Input background nền trắng, border mảnh, radius theo guide.
- `docs/frontend_phase_change_log.md`
  - Tạo file log duy nhất cho các phase frontend.

### 3. Design source đã áp dụng

- Đã đọc `docs/FE_Architecture.md`.
- Đã đọc `docs/SMARTRENT_MOBILE_UI_GUIDE.md`.
- Đã đọc SKILL.md / Taste Skill (`design-taste-frontend`) và chỉ áp dụng như rule phụ về hierarchy/spacing/polish.
- Đã đọc `docs/phase0_frontend_audit.md`.
- Đã đọc `docs/phase1a_frontend_foundation_report.md`.

Ưu tiên áp dụng: `SMARTRENT_MOBILE_UI_GUIDE.md` và Android XML/Kotlin architecture. Taste Skill không được dùng để đưa web/Tailwind/React/Compose vào project.

### 4. Resource/design token đã thêm

#### Color token

- Brand/primary:
  - `sr_primary`
  - `sr_primary_dark`
  - `sr_primary_light`
- Secondary/accent:
  - `sr_secondary`
- Background/surface:
  - `sr_background`
  - `sr_surface`
  - `sr_surface_elevated`
- Text:
  - `sr_text_primary`
  - `sr_text_secondary`
  - `sr_text_muted`
- Border/divider:
  - `sr_border`
  - `sr_divider`
- Semantic/status:
  - `sr_success`
  - `sr_success_container`
  - `sr_warning`
  - `sr_warning_container`
  - `sr_error`
  - `sr_error_container`
  - `sr_info`
  - `sr_info_container`
  - `sr_neutral_container`
  - `sr_vacant`
  - `sr_vacant_container`

Các màu chính bám theo UI guide. `sr_surface_elevated`, `sr_divider`, container colors và neutral container là bổ sung trung tính để hỗ trợ XML component foundation.

#### Dimens token

- Spacing:
  - `space_2`
  - `space_4`
  - `space_8`
  - `space_12`
  - `space_16`
  - `space_20`
  - `space_24`
  - `space_32`
- Radius:
  - `radius_sm`
  - `radius_md`
  - `radius_lg`
  - `radius_xl`
  - `radius_full`
- Component size:
  - `button_height`
  - `input_height`
  - `bottom_nav_height`
  - `toolbar_height`
  - `card_min_height`
  - `chip_min_height`
- Stroke/elevation:
  - `stroke_thin`
  - `elevation_card`
  - `elevation_raised`
  - `elevation_sheet`
- Text size:
  - `text_xs`
  - `text_sm`
  - `text_md`
  - `text_lg`
  - `text_xl`
  - `text_2xl`

#### Typography style

- `TextAppearance.SmartRent.TitleLarge`
- `TextAppearance.SmartRent.TitleMedium`
- `TextAppearance.SmartRent.SectionTitle`
- `TextAppearance.SmartRent.Body`
- `TextAppearance.SmartRent.BodySecondary`
- `TextAppearance.SmartRent.Caption`

Các style dùng system font trước, không thêm font file ngoài và không tải font từ internet.

#### Component style

- Button:
  - `Widget.SmartRent.Button.Primary`
  - `Widget.SmartRent.Button.Secondary`
  - `Widget.SmartRent.Button.Text`
- Input:
  - `Widget.SmartRent.TextInputLayout`
  - `Widget.SmartRent.TextInputEditText`
- Card:
  - `Widget.SmartRent.Card`
  - `Widget.SmartRent.Card.Elevated`
  - `Widget.SmartRent.Card.Compact`
- Toolbar/Header:
  - `Widget.SmartRent.Toolbar`

#### Drawable nền

- `bg_app.xml`
- `bg_card.xml`
- `bg_chip.xml`
- `bg_input.xml`

### 5. Theme/style status

- App theme vẫn dùng `Theme.Material3.DayNight.NoActionBar`.
- `colorPrimary`, `colorSecondary`, `colorSurface`, `colorError`, `colorOnPrimary`, `colorOnSurface` đã trỏ về token SmartRent.
- Light theme giữ `windowLightStatusBar=true`.
- Night theme đồng bộ token cơ bản, giữ an toàn và chưa cố dựng dark mode hoàn chỉnh.
- Không làm hỏng `AppCompatActivity`, manifest hoặc navigation foundation.

### 6. Build status

Lệnh chuẩn đã chạy:

```powershell
.\gradlew.bat :app:assembleDebug
```

Kết quả:

- Fail do terminal chưa có `JAVA_HOME` và không tìm thấy `java` trong `PATH`.

Lệnh build kiểm chứng bằng Android Studio JBR tạm:

```powershell
$env:JAVA_HOME='C:\Program Files\Android\Android Studio\jbr'
$env:Path="$env:JAVA_HOME\bin;$env:Path"
.\gradlew.bat :app:assembleDebug
```

Kết quả:

- `BUILD SUCCESSFUL`.
- Các task quan trọng đã chạy qua: `generateDebugResources`, `processDebugNavigationResources`, `mergeDebugResources`, `compileDebugKotlin`, `processDebugResources`, `packageDebug`, `assembleDebug`.

### 7. Vấn đề còn lại

#### Critical

- Không có.

#### High

- Cần cấu hình `JAVA_HOME` ổn định cho terminal/dev machine để build bằng lệnh chuẩn mà không cần set JBR tạm.

#### Medium

- Design System mới ở mức foundation. Các component thực tế, state UI, error/loading/empty state và màn hình skeleton chi tiết nên xử lý ở Phase 1C+.
- Dark mode mới đồng bộ token an toàn, chưa polish đầy đủ.

#### Low

- `app_name` vẫn là `Rent`.
- Token cũ `color_*` từ Phase 1A vẫn được giữ để tránh phá compatibility; có thể dọn dần khi các màn hình đã chuyển sang `sr_*`.

### 8. Kết luận

**READY_FOR_PHASE_1C**

- `assembleDebug` build thành công khi dùng JBR hợp lệ.
- Design token foundation đã có.
- Theme/resource/style compile được.
- Component style nền đủ để dùng cho skeleton màn hình sau.
- Không vi phạm stack Android Native XML + Kotlin.

## Phase 1C - App Navigation + Screen Skeletons

### 1. Mục tiêu phase

Phase 1C dựng app shell, navigation graph, bottom navigation foundation và các screen skeleton chính để các phase sau triển khai UI chi tiết theo từng module.

Phase này không dựng UI hoàn chỉnh, không code form thật, không code dashboard/danh sách thật, không kết nối API, không tạo repository thật, không thêm Retrofit/Room/Hilt/DataStore, không đổi `namespace`/`applicationId`, không dùng Compose và không dùng web tech.

### 2. File đã tạo/sửa

#### File đã sửa

- `docs/frontend_phase_change_log.md`
  - Backfill mục Phase 1A ngắn vì log chung được tạo từ Phase 1B.
  - Append mục Phase 1C.
- `app/src/main/java/com/example/rent/MainActivity.kt`
  - Setup `NavController`.
  - Kết nối `BottomNavigationView` với Navigation UI.
  - Ẩn bottom navigation ngoài các main tab.
- `app/src/main/java/com/example/rent/navigation/AppRoutes.kt`
  - Bổ sung route constants cho auth, main tabs, detail và notifications skeleton.
- `app/src/main/res/layout/activity_main.xml`
  - Thêm `BottomNavigationView`.
  - Ràng buộc `NavHostFragment` phía trên bottom nav.
- `app/src/main/res/navigation/main_nav_graph.xml`
  - Đổi start destination sang `LoginFragment`.
  - Thêm routes cho auth, main tabs, detail và notifications skeleton.
- Các Fragment có sẵn từ Phase 1A:
  - `AuthFragment`
  - `HomeFragment`
  - `RoomsFragment`
  - `TenantsFragment`
  - `BillsFragment`
  - `ProfileFragment`
  - Chuyển sang ViewBinding lifecycle-safe.
- Các layout có sẵn từ Phase 1A:
  - `fragment_auth.xml`
  - `fragment_home.xml`
  - `fragment_rooms.xml`
  - `fragment_tenants.xml`
  - `fragment_bills.xml`
  - `fragment_profile.xml`
  - Chuẩn hóa thành skeleton gồm title, mô tả ngắn và content placeholder card.
- `app/src/main/res/values/strings.xml`
  - Thêm title, bottom nav label, placeholder text cho các skeleton screen.

#### File đã tạo mới

- Auth screens:
  - `app/src/main/java/com/example/rent/presentation/auth/LoginFragment.kt`
  - `app/src/main/java/com/example/rent/presentation/auth/RegisterFragment.kt`
  - `app/src/main/java/com/example/rent/presentation/auth/ForgotPasswordFragment.kt`
- Detail/notification screens:
  - `app/src/main/java/com/example/rent/presentation/rooms/RoomDetailFragment.kt`
  - `app/src/main/java/com/example/rent/presentation/tenants/TenantDetailFragment.kt`
  - `app/src/main/java/com/example/rent/presentation/bills/BillDetailFragment.kt`
  - `app/src/main/java/com/example/rent/presentation/notifications/NotificationsFragment.kt`
- Skeleton layouts:
  - `app/src/main/res/layout/fragment_login.xml`
  - `app/src/main/res/layout/fragment_register.xml`
  - `app/src/main/res/layout/fragment_forgot_password.xml`
  - `app/src/main/res/layout/fragment_room_detail.xml`
  - `app/src/main/res/layout/fragment_tenant_detail.xml`
  - `app/src/main/res/layout/fragment_bill_detail.xml`
  - `app/src/main/res/layout/fragment_notifications.xml`
- Bottom navigation resources:
  - `app/src/main/res/menu/bottom_nav_menu.xml`
  - `app/src/main/res/color/bottom_nav_item_color.xml`
  - `app/src/main/res/drawable/ic_nav_home.xml`
  - `app/src/main/res/drawable/ic_nav_rooms.xml`
  - `app/src/main/res/drawable/ic_nav_tenants.xml`
  - `app/src/main/res/drawable/ic_nav_bills.xml`
  - `app/src/main/res/drawable/ic_nav_profile.xml`

### 3. Design source đã áp dụng

- Đã đọc `docs/FE_Architecture.md`.
- Đã đọc `docs/SMARTRENT_MOBILE_UI_GUIDE.md`.
- Đã đọc SKILL.md / Taste Skill (`design-taste-frontend`) và chỉ áp dụng như rule phụ về hierarchy/spacing/polish.
- Đã đọc `docs/phase0_frontend_audit.md`.
- Đã đọc `docs/frontend_phase_change_log.md`.

Ưu tiên áp dụng: Android Native XML + Kotlin, Navigation Component, design token/style từ Phase 1B. Không đưa Compose, Tailwind, React hoặc web CSS vào project.

### 4. Navigation đã dựng

- Navigation graph file: `app/src/main/res/navigation/main_nav_graph.xml`.
- Start destination: `LoginFragment`.
- Lý do: chưa có auth/session state thật, nên app bắt đầu ở skeleton đăng nhập. Auth routing thật sẽ xử lý ở phase sau.
- Fragment routes đã khai báo:
  - `authFragment`
  - `loginFragment`
  - `registerFragment`
  - `forgotPasswordFragment`
  - `homeFragment`
  - `roomsFragment`
  - `roomDetailFragment`
  - `tenantsFragment`
  - `tenantDetailFragment`
  - `billsFragment`
  - `billDetailFragment`
  - `profileFragment`
  - `notificationsFragment`
- Bottom navigation status:
  - Đã tạo foundation bằng `BottomNavigationView`.
  - Đã kết nối với `NavController` qua Navigation UI.
  - Bottom nav hiển thị trên main tabs và ẩn trên auth/detail/notifications destinations.
- Tab chính:
  - Tổng quan
  - Phòng
  - Người thuê
  - Hóa đơn
  - Cá nhân

### 5. Screen skeleton đã tạo

- Auth screens:
  - `LoginFragment`
  - `RegisterFragment`
  - `ForgotPasswordFragment`
  - `AuthFragment` giữ lại như placeholder legacy từ Phase 1A.
- Main screens:
  - `HomeFragment`
  - `RoomsFragment`
  - `TenantsFragment`
  - `BillsFragment`
  - `ProfileFragment`
- Detail/notification screens:
  - `RoomDetailFragment`
  - `TenantDetailFragment`
  - `BillDetailFragment`
  - `NotificationsFragment`

Mỗi Fragment dùng ViewBinding, chưa có ViewModel phức tạp, chưa có API, chưa có dữ liệu thật, chưa có adapter.

### 6. Resource/string/layout status

- Strings đã thêm:
  - Screen title cho auth, main tabs, detail, notifications.
  - Bottom nav labels.
  - Placeholder title/subtitle/content text bằng tiếng Việt.
- Layout XML đã tạo/cập nhật:
  - Root container.
  - Title.
  - Short description.
  - Placeholder content area bằng `MaterialCardView`.
- Menu/vector/color đã tạo:
  - `bottom_nav_menu.xml`.
  - `bottom_nav_item_color.xml`.
  - 5 vector icon tối giản cho bottom nav.

### 7. Build status

Lệnh chuẩn đã chạy:

```powershell
.\gradlew.bat :app:assembleDebug
```

Kết quả:

- Fail do terminal chưa có `JAVA_HOME` và không tìm thấy `java` trong `PATH`.

Lệnh build kiểm chứng bằng Android Studio JBR tạm:

```powershell
$env:JAVA_HOME='C:\Program Files\Android\Android Studio\jbr'
$env:Path="$env:JAVA_HOME\bin;$env:Path"
.\gradlew.bat :app:assembleDebug
```

Kết quả:

- `BUILD SUCCESSFUL`.
- Các task quan trọng đã chạy qua: `processDebugNavigationResources`, `compileDebugNavigationResources`, `mergeDebugResources`, `dataBindingGenBaseClassesDebug`, `compileDebugKotlin`, `packageDebug`, `assembleDebug`.

### 8. Vấn đề còn lại

#### Critical

- Không có.

#### High

- Cần cấu hình `JAVA_HOME` ổn định cho terminal/dev machine để build bằng lệnh chuẩn mà không cần set JBR tạm.

#### Medium

- Chưa có auth/session state thật để quyết định start destination động.
- Chưa có action flow giữa Login/Register/Forgot và main app.
- Chưa có navigation arguments cho detail screens.
- Bottom nav icon/label mới ở mức foundation, có thể polish sau.

#### Low

- `AuthFragment` legacy từ Phase 1A vẫn được giữ lại nhưng không phải start destination.
- `app_name` vẫn là `Rent`.

### 9. Kết luận

**READY_FOR_PHASE_2A**

- `assembleDebug` build thành công khi dùng JBR hợp lệ.
- Navigation graph compile và khai báo đầy đủ skeleton routes.
- `MainActivity` setup `NavController` và bottom navigation không lỗi build.
- Fragment skeleton build được và dùng ViewBinding.
- Bottom navigation foundation build được.
- Không vi phạm stack Android Native XML + Kotlin.

## Phase 2A - Auth UI

### 1. Mục tiêu phase

Phase 2A triển khai UI thật cho luồng Auth cơ bản gồm Đăng nhập, Đăng ký và Quên mật khẩu theo Android Native XML + Kotlin.

Phase này không kết nối API, không tạo Retrofit/repository/token/session storage, không thêm database, không thay đổi backend/business logic, không tạo màn Home/Rooms/Tenants/Bills/Profile thật, không dùng Compose và không dùng web technology.

### 2. File đã sửa

- `app/src/main/res/layout/fragment_login.xml`
  - Thay skeleton bằng form đăng nhập thật dùng `NestedScrollView`, `TextInputLayout`, `TextInputEditText` và `MaterialButton`.
  - Thêm header SmartRent, title/subtitle, email/số điện thoại, mật khẩu, quên mật khẩu, đăng nhập, tạo tài khoản và trust/support text.
- `app/src/main/res/layout/fragment_register.xml`
  - Thay skeleton bằng form đăng ký gồm họ tên, email/số điện thoại, mật khẩu, nhập lại mật khẩu, nút đăng ký, terms note và quay lại đăng nhập.
- `app/src/main/res/layout/fragment_forgot_password.xml`
  - Thay skeleton bằng form khôi phục mật khẩu gồm email/số điện thoại, gửi yêu cầu, note placeholder và quay lại đăng nhập.
- `app/src/main/java/com/example/rent/presentation/auth/LoginFragment.kt`
  - Thêm ViewBinding click listeners cho Login -> Register, Login -> Forgot Password và Login -> Home mock.
  - Thêm validation rỗng cho contact/password.
- `app/src/main/java/com/example/rent/presentation/auth/RegisterFragment.kt`
  - Thêm click quay lại đăng nhập.
  - Thêm validation rỗng cho họ tên/contact/password/confirm password và kiểm tra password mismatch.
  - Sau validation hợp lệ chỉ quay lại Login như placeholder local, không tạo tài khoản thật.
- `app/src/main/java/com/example/rent/presentation/auth/ForgotPasswordFragment.kt`
  - Thêm click quay lại đăng nhập.
  - Thêm validation rỗng cho contact.
  - Sau validation hợp lệ chỉ quay lại Login như placeholder local, không gửi request thật.
- `app/src/main/res/navigation/main_nav_graph.xml`
  - Thêm action `action_login_to_register`.
  - Thêm action `action_login_to_forgot_password`.
  - Thêm action `action_register_to_login`.
  - Thêm action `action_forgot_password_to_login`.
  - Thêm action mock `action_login_to_home`.
- `app/src/main/res/values/strings.xml`
  - Thêm strings tiếng Việt cho Auth UI, hint, button, note và validation error.
- `docs/frontend_phase_change_log.md`
  - Thêm log Phase 2A.

### 3. Design source đã áp dụng

- Đã đọc `docs/FE_Architecture.md`.
- Đã đọc `docs/SMARTRENT_MOBILE_UI_GUIDE.md`.
- Đã đọc `docs/phase0_frontend_audit.md`.
- Đã đọc Taste Skill (`design-taste-frontend`) và chỉ dùng như rule phụ về spacing/hierarchy/polish.

Ưu tiên áp dụng: `SMARTRENT_MOBILE_UI_GUIDE.md`, XML design tokens/styles đã có từ Phase 1B, kiến trúc Single Activity + Fragment Navigation Component từ Phase 1C.

### 4. Auth UI đã triển khai

- Login:
  - Header SmartRent.
  - Title/subtitle.
  - Input email hoặc số điện thoại.
  - Input mật khẩu có password toggle.
  - Link quên mật khẩu.
  - Button đăng nhập.
  - Secondary action tạo tài khoản.
  - Trust/support text.
- Register:
  - Header SmartRent.
  - Title/subtitle.
  - Input họ tên.
  - Input email hoặc số điện thoại.
  - Input mật khẩu.
  - Input nhập lại mật khẩu.
  - Button đăng ký.
  - Terms note.
  - Back to login action.
- Forgot password:
  - Header SmartRent.
  - Title/subtitle.
  - Input email hoặc số điện thoại.
  - Button gửi yêu cầu.
  - Placeholder note về backend/auth sau.
  - Back to login action.

### 5. Navigation Auth

- Login -> Register: `action_login_to_register`.
- Login -> Forgot Password: `action_login_to_forgot_password`.
- Login -> Home mock: `action_login_to_home`.
- Register -> Login: click dùng `popBackStack(R.id.loginFragment, false)` để quay lại Login hiện có; nếu không có Login trong back stack thì fallback qua `action_register_to_login`.
- Forgot Password -> Login: click dùng `popBackStack(R.id.loginFragment, false)` để quay lại Login hiện có; nếu không có Login trong back stack thì fallback qua `action_forgot_password_to_login`.
- Graph vẫn khai báo `action_register_to_login` và `action_forgot_password_to_login` để giữ route rõ ràng cho phase sau.

`action_login_to_home` chỉ là mock local sau validation để kiểm chứng app shell. Không có token, không có session, không có API và không xác thực thật.

### 6. Validation behavior

- Login:
  - Báo lỗi khi contact trống.
  - Báo lỗi khi password trống.
  - Nếu hợp lệ thì navigate mock tới Home.
- Register:
  - Báo lỗi khi họ tên/contact/password/confirm password trống.
  - Báo lỗi khi confirm password không khớp.
  - Nếu hợp lệ thì quay lại Login, không tạo user thật.
- Forgot password:
  - Báo lỗi khi contact trống.
  - Nếu hợp lệ thì quay lại Login, không gửi request thật.

### 7. Build status

Lệnh chuẩn đã chạy:

```powershell
.\gradlew.bat :app:assembleDebug
```

Kết quả:

- Fail do terminal chưa có `JAVA_HOME` và không tìm thấy `java` trong `PATH`.
- Đây là lỗi cấu hình môi trường, không phải lỗi source Phase 2A.

Lệnh build kiểm chứng bằng Android Studio JBR tạm:

```powershell
$env:JAVA_HOME='C:\Program Files\Android\Android Studio\jbr'
$env:Path="$env:JAVA_HOME\bin;$env:Path"
.\gradlew.bat :app:assembleDebug
```

Kết quả:

- `BUILD SUCCESSFUL`.
- Các task quan trọng đã chạy qua: `processDebugNavigationResources`, `compileDebugNavigationResources`, `mergeDebugResources`, `dataBindingGenBaseClassesDebug`, `compileDebugKotlin`, `compileDebugJavaWithJavac`, `packageDebug`, `assembleDebug`.

### 8. Vấn đề còn lại

#### Critical

- Không có lỗi source/build blocker khi dùng JBR hợp lệ.

#### High

- Cần cấu hình `JAVA_HOME` ổn định cho terminal/dev machine để build bằng lệnh chuẩn mà không cần set Android Studio JBR tạm.

#### Medium

- Auth hiện chỉ có UI và validation local, chưa có API/session/token thật.
- Register/Forgot Password đang quay lại Login như placeholder local sau validation hợp lệ.
- Chưa có trạng thái loading/success/error thật từ backend.

#### Low

- `app_name` vẫn là `Rent`.
- Auth copy/text có thể tinh chỉnh thêm ở phase polish sau.

### 9. Kết luận

**READY_FOR_PHASE_2B**

- Auth UI XML thật đã có cho Login/Register/Forgot Password.
- Auth navigation nội bộ đã có.
- ViewBinding lifecycle vẫn an toàn.
- Build source thành công khi dùng JBR hợp lệ.
- Không vi phạm stack Android Native XML + Kotlin.
- Không triển khai API/token/session/backend/business logic.

## Phase 2B - Home / Dashboard UI

### 1. Mục tiêu phase

Phase 2B dựng UI thật cho màn Home/Dashboard của SmartRent bằng Android Native XML + Kotlin. Màn hình hiển thị tổng quan phòng, người thuê, hóa đơn, doanh thu, thao tác nhanh, nhắc việc và hoạt động gần đây bằng dữ liệu mock/static.

Phase này không kết nối API, không tạo Retrofit/repository/database/storage, không tạo ViewModel phức tạp, không sửa backend, không dựng chi tiết Rooms/Tenants/Bills/Profile, không thay đổi nghiệp vụ, không dùng Compose và không dùng web technology.

### 2. File đã tạo/sửa

- `app/src/main/res/layout/fragment_home.xml`
  - Thay skeleton Home bằng dashboard scrollable thật.
  - Thêm header, overview cards, quick actions, billing reminder và recent activity.
- `app/src/main/java/com/example/rent/presentation/home/HomeFragment.kt`
  - Thêm click listeners bằng ViewBinding.
  - Điều hướng tới route đã có: Rooms, Tenants, Bills, Notifications.
  - Disable action ghi điện/nước vì chưa có route riêng, kèm TODO rõ cho phase sau.
- `app/src/main/res/navigation/main_nav_graph.xml`
  - Thêm action `action_home_to_rooms`.
  - Thêm action `action_home_to_tenants`.
  - Thêm action `action_home_to_bills`.
  - Thêm action `action_home_to_notifications`.
- `app/src/main/res/values/strings.xml`
  - Thêm strings tiếng Việt cho Home/Dashboard, overview cards, quick actions, reminder và recent activity.
- `app/src/main/res/drawable/ic_home_notifications.xml`
  - Thêm vector icon notification tối giản cho header action.
- `docs/frontend_phase_change_log.md`
  - Thêm log Phase 2B.

### 3. Design source đã áp dụng

- Đã đọc `docs/FE_Architecture.md`.
- Đã đọc `docs/SMARTRENT_MOBILE_UI_GUIDE.md`.
- Đã đọc SKILL.md / Taste Skill (`design-taste-frontend`) và chỉ dùng phần phù hợp về hierarchy/spacing/polish.
- Đã đọc `docs/phase0_frontend_audit.md`.
- Đã đọc `docs/frontend_phase_change_log.md`.

Ưu tiên áp dụng: `SMARTRENT_MOBILE_UI_GUIDE.md`, Android Native XML + Kotlin, ViewBinding, Navigation Component và design tokens/styles từ Phase 1B. Không dùng Compose, Tailwind, React hoặc web CSS.

### 4. Home/Dashboard UI đã dựng

- Header:
  - Lời chào chủ nhà.
  - Subtitle tổng quan hôm nay.
  - Card tên nhà trọ mock.
  - Avatar chữ `SR`.
  - Notification action đi tới Notifications route.
- Overview cards:
  - Tổng số phòng.
  - Phòng đang thuê.
  - Phòng trống.
  - Người thuê hiện tại.
  - Hóa đơn chờ thu.
  - Thu tháng này.
- Quick actions:
  - Thêm phòng.
  - Thêm người thuê.
  - Tạo hóa đơn.
  - Ghi điện/nước, đang disabled vì chưa có route riêng.
- Billing reminder:
  - Card nhắc hóa đơn sắp đến hạn, phòng chưa chốt điện/nước và khoản thu cần xử lý.
  - Action xem hóa đơn đi tới Bills route.
- Recent activity:
  - Người thuê mới.
  - Hóa đơn đã tạo.
  - Phòng vừa cập nhật.
- Style/resource đã dùng:
  - `bg_app`, `bg_chip`.
  - `Widget.SmartRent.Card`, `Widget.SmartRent.Card.Compact`.
  - `Widget.SmartRent.Button.Primary`, `Widget.SmartRent.Button.Secondary`, `Widget.SmartRent.Button.Text`.
  - `TextAppearance.SmartRent.*`.
  - `sr_*` color tokens và `space_*` dimens.

### 5. Navigation Home

- Home route đã tồn tại: `homeFragment`.
- Quick action navigation:
  - Home -> Rooms: `action_home_to_rooms`.
  - Home -> Tenants: `action_home_to_tenants`.
  - Home -> Bills: `action_home_to_bills`.
  - Home -> Notifications: `action_home_to_notifications`.
- Bottom navigation:
  - Home tab vẫn dùng `homeFragment`.
  - Không redesign bottom nav.
  - Không đổi main destinations.
- Mock navigation:
  - Login -> Home mock từ Phase 2A vẫn giữ nguyên.
  - Home quick actions chỉ điều hướng tới skeleton route đã có, chưa mở form thật.

### 6. UI behavior

- Click listener đã thêm:
  - `notificationButton` -> Notifications.
  - `addRoomButton` -> Rooms.
  - `addTenantButton` -> Tenants.
  - `createBillButton` -> Bills.
  - `openBillsReminderButton` -> Bills.
- `recordMeterButton` bị disable vì chưa có meter-reading route trong Phase 2B.
- Dữ liệu đang là mock/static trong resource strings.
- Không gọi API, không tạo repository, không tạo ViewModel thật, không lưu dữ liệu và không xử lý business logic thật.

### 7. Resource/string/drawable status

- Strings đã thêm:
  - Home title/subtitle/header.
  - Overview card label/value/hint.
  - Quick action label.
  - Billing reminder title/content/action.
  - Recent activity title/content/time.
- Drawable/icon đã thêm:
  - `ic_home_notifications.xml`.
- Resource token đã dùng:
  - `sr_primary`, `sr_primary_light`, `sr_warning`, `sr_warning_container`, `sr_vacant`, `sr_vacant_container`, `sr_divider`.
  - `space_4`, `space_8`, `space_12`, `space_16`, `space_24`, `space_32`, `stroke_thin`, `button_height`.

### 8. Build status

Lệnh chuẩn đã chạy:

```powershell
.\gradlew.bat :app:assembleDebug
```

Kết quả:

- Fail do terminal chưa có `JAVA_HOME` và không tìm thấy `java` trong `PATH`.
- Đây là lỗi cấu hình môi trường, không phải lỗi source Phase 2B.

Lệnh build kiểm chứng bằng Android Studio JBR tạm:

```powershell
$env:JAVA_HOME='C:\Program Files\Android\Android Studio\jbr'
$env:Path="$env:JAVA_HOME\bin;$env:Path"
.\gradlew.bat :app:assembleDebug
```

Kết quả:

- `BUILD SUCCESSFUL`.
- Các task quan trọng đã chạy qua: `processDebugNavigationResources`, `compileDebugNavigationResources`, `mergeDebugResources`, `dataBindingGenBaseClassesDebug`, `compileDebugKotlin`, `compileDebugJavaWithJavac`, `packageDebug`, `assembleDebug`.

### 9. Vấn đề còn lại

#### Critical

- Không có lỗi source/build blocker khi dùng JBR hợp lệ.

#### High

- Cần cấu hình `JAVA_HOME` ổn định cho terminal/dev machine để build bằng lệnh chuẩn mà không cần set Android Studio JBR tạm.

#### Medium

- Home đang dùng dữ liệu mock/static.
- Action ghi điện/nước chưa có route riêng nên đang disabled.
- Quick actions đang điều hướng tới skeleton route, chưa mở form tạo thật.
- Chưa có loading/empty/error state từ backend vì chưa kết nối API.

#### Low

- `app_name` vẫn là `Rent`.
- Dashboard có thể polish thêm icon/spacing vi mô khi có dữ liệu thật.

### 10. Kết luận

**READY_FOR_PHASE_2C**

- `assembleDebug` build thành công khi dùng JBR hợp lệ.
- Home/Dashboard UI đã hiển thị được bằng XML.
- Navigation Home không lỗi build.
- Bottom navigation không bị thay đổi.
- Không gọi API thật.
- Không vi phạm XML + Kotlin stack.
- Không phá Auth UI/design system.

## Phase 2C - Rooms UI

### 1. Mục tiêu phase

Phase 2C dựng UI thật cho module Quản lý phòng của SmartRent bằng Android Native XML + Kotlin. Phase này bao gồm Rooms list, status/filter UI, room card UI, Room Detail UI, Add/Edit Room UI skeleton, navigation nội bộ Rooms và dữ liệu mock/static để hiển thị.

Phase này không kết nối API, không tạo Retrofit service, không tạo Room database/local storage, không tạo repository thật, không sửa backend, không xử lý nghiệp vụ thật, không dựng Tenants/Bills/Profile chi tiết, không dùng Compose và không dùng web technology.

### 2. File đã tạo/sửa

- `app/src/main/res/layout/fragment_rooms.xml`
  - Thay skeleton Rooms bằng màn danh sách phòng thật.
  - Thêm header, summary cards, filter chips, room cards và empty state placeholder.
- `app/src/main/res/layout/fragment_room_detail.xml`
  - Thay skeleton Room Detail bằng UI chi tiết phòng thật.
  - Thêm header/status, room information, tenant summary, billing summary và actions.
- `app/src/main/res/layout/fragment_add_edit_room.xml`
  - Tạo form Add/Edit Room bằng Material TextInputLayout/TextInputEditText.
  - Thêm fields: tên phòng, giá thuê, sức chứa, diện tích, tầng/khu, trạng thái placeholder, ghi chú, lưu và hủy.
- `app/src/main/java/com/example/rent/presentation/rooms/RoomsFragment.kt`
  - Thêm ViewBinding click listeners cho Add Room và Room Detail.
- `app/src/main/java/com/example/rent/presentation/rooms/RoomDetailFragment.kt`
  - Thêm click listeners cho Edit Room, Bills, Tenants và disable meter action khi chưa có route riêng.
- `app/src/main/java/com/example/rent/presentation/rooms/AddEditRoomFragment.kt`
  - Tạo Fragment mới dùng ViewBinding lifecycle-safe.
  - Thêm validation nhẹ cho tên phòng và giá thuê.
  - Save/cancel chỉ popBackStack, không lưu dữ liệu thật.
- `app/src/main/res/navigation/main_nav_graph.xml`
  - Thêm route `addEditRoomFragment`.
  - Thêm action `action_rooms_to_room_detail`.
  - Thêm action `action_rooms_to_add_room`.
  - Thêm action `action_room_detail_to_edit_room`.
  - Thêm action `action_room_detail_to_tenants`.
  - Thêm action `action_room_detail_to_bills`.
- `app/src/main/java/com/example/rent/navigation/AppRoutes.kt`
  - Thêm route constant `ADD_EDIT_ROOM`.
- `app/src/main/res/values/strings.xml`
  - Thêm strings tiếng Việt cho Rooms list, filters, cards, detail, Add/Edit form và validation.
- `docs/frontend_phase_change_log.md`
  - Thêm log Phase 2C.

### 3. Design source đã áp dụng

- Đã đọc `docs/FE_Architecture.md`.
- Đã đọc `docs/SMARTRENT_MOBILE_UI_GUIDE.md`.
- Đã đọc SKILL.md / Taste Skill (`design-taste-frontend`) và chỉ dùng phần phù hợp về hierarchy/spacing/polish.
- Đã đọc `docs/phase0_frontend_audit.md`.
- Đã đọc `docs/frontend_phase_change_log.md`.

Ưu tiên áp dụng: `SMARTRENT_MOBILE_UI_GUIDE.md`, Android Native XML + Kotlin, ViewBinding, Navigation Component và design tokens/styles từ Phase 1B. Không dùng Compose, Tailwind, React hoặc web CSS.

### 4. Rooms UI đã dựng

- Rooms list:
  - Header “Quản lý phòng”.
  - Subtitle mô tả trạng thái phòng.
  - Action thêm phòng.
  - Danh sách card tĩnh cho Phòng 101, 203, 305 bằng mock/static data.
- Summary cards:
  - Tổng phòng.
  - Đang thuê.
  - Còn trống.
  - Cần xử lý.
- Filter/status chips:
  - Tất cả.
  - Đang thuê.
  - Còn trống.
  - Bảo trì.
  - Cần xử lý.
- Room item/card:
  - Tên phòng.
  - Trạng thái.
  - Số người/trạng thái sử dụng.
  - Giá phòng.
  - Ghi chú hóa đơn/bảo trì.
  - CTA xem chi tiết cho card chính.
- Empty state:
  - Có `roomsEmptyStateCard` đang `gone` để chuẩn bị khi list rỗng.
  - Có message và button thêm phòng.
- Resource/style đã dùng:
  - `bg_app`, `bg_chip`.
  - `Widget.SmartRent.Card`, `Widget.SmartRent.Card.Compact`.
  - `Widget.SmartRent.Button.Primary`, `Widget.SmartRent.Button.Secondary`, `Widget.SmartRent.Button.Text`.
  - `TextAppearance.SmartRent.*`.
  - `sr_*` color tokens và `space_*` dimens.

### 5. Room Detail UI

- Header/status:
  - Tên phòng mock `Phòng 101`.
  - Subtitle ngắn.
  - Status chip `Đang thuê`.
  - Action sửa phòng.
- Room info:
  - Giá thuê.
  - Diện tích.
  - Tầng/khu.
  - Sức chứa.
  - Số người hiện tại.
  - Ghi chú ngắn.
- Tenant summary:
  - Người thuê hiện tại.
  - Số thành viên.
  - Ngày bắt đầu thuê mock.
- Billing summary:
  - Tiền phòng.
  - Chỉ số điện/nước gần nhất.
  - Trạng thái hóa đơn.
  - CTA xem hóa đơn đi tới Bills route.
- Actions đã chuẩn bị:
  - Sửa phòng.
  - Thêm người thuê.
  - Tạo hóa đơn.
  - Ghi điện/nước, đang disabled vì chưa có meter route riêng.

### 6. Add/Edit Room UI

- Form fields:
  - Tên phòng.
  - Giá thuê.
  - Sức chứa.
  - Diện tích.
  - Tầng/khu.
  - Trạng thái phòng placeholder.
  - Ghi chú.
- Button/actions:
  - Lưu thông tin.
  - Hủy.
- Validation nhẹ:
  - Tên phòng không được để trống.
  - Giá thuê không được để trống.
  - Validation chỉ hiển thị lỗi UI, không lưu dữ liệu thật.

### 7. Navigation Rooms

- Routes đã thêm/sửa:
  - `roomsFragment` giữ nguyên.
  - `roomDetailFragment` giữ nguyên.
  - Thêm `addEditRoomFragment`.
- Actions đã thêm/sửa:
  - `action_rooms_to_room_detail`.
  - `action_rooms_to_add_room` với `formMode=add`.
  - `action_room_detail_to_edit_room` với `formMode=edit`.
  - `action_room_detail_to_tenants`.
  - `action_room_detail_to_bills`.
- Bottom navigation status:
  - Rooms tab vẫn dùng `roomsFragment`.
  - Không redesign bottom nav.
  - Không đổi main destinations.
- Mock argument/navigation:
  - Chỉ dùng argument String đơn giản `formMode` để đổi title Add/Edit.
  - Không truyền roomId thật và không thêm deep link.

### 8. UI behavior

- Click listener đã thêm:
  - Rooms Add Room -> Add/Edit Room.
  - Empty Add Room -> Add/Edit Room.
  - Room cards -> Room Detail.
  - Room Detail Edit -> Add/Edit Room mode edit.
  - Room Detail Bills actions -> Bills route.
  - Room Detail Add Tenant -> Tenants route.
- Dữ liệu đang là mock/static trong XML strings.
- Không gọi API, không tạo repository, không lưu dữ liệu, không xử lý nghiệp vụ thật.
- Không dùng RecyclerView trong Phase 2C để tránh thêm dependency/nền kỹ thuật ngoài phạm vi; list hiện là XML card tĩnh phục vụ UI foundation.

### 9. Resource/string/drawable status

- Strings đã thêm:
  - Rooms title/subtitle.
  - Summary labels/values.
  - Filter labels.
  - Room card labels/status/content.
  - Room Detail labels/content/actions.
  - Add/Edit Room form labels/actions/errors.
- Drawable/icon đã thêm:
  - Không thêm drawable mới trong Phase 2C.
  - Dùng lại `bg_app` và `bg_chip` từ Phase 1B.
- Resource token đã dùng:
  - `sr_primary`, `sr_info_container`, `sr_vacant`, `sr_vacant_container`, `sr_warning`, `sr_warning_container`.
  - `space_4`, `space_8`, `space_12`, `space_16`, `space_20`, `space_24`, `space_32`.

### 10. Build status

Lệnh chuẩn đã chạy:

```powershell
.\gradlew.bat :app:assembleDebug
```

Kết quả:

- Fail do terminal chưa có `JAVA_HOME` và không tìm thấy `java` trong `PATH`.
- Đây là lỗi cấu hình môi trường, không phải lỗi source Phase 2C.

Lệnh build kiểm chứng bằng Android Studio JBR tạm:

```powershell
$env:JAVA_HOME='C:\Program Files\Android\Android Studio\jbr'
$env:Path="$env:JAVA_HOME\bin;$env:Path"
.\gradlew.bat :app:assembleDebug
```

Kết quả:

- `BUILD SUCCESSFUL`.
- Các task quan trọng đã chạy qua: `processDebugNavigationResources`, `compileDebugNavigationResources`, `mergeDebugResources`, `dataBindingGenBaseClassesDebug`, `compileDebugKotlin`, `compileDebugJavaWithJavac`, `packageDebug`, `assembleDebug`.

### 11. Vấn đề còn lại

#### Critical

- Không có lỗi source/build blocker khi dùng JBR hợp lệ.

#### High

- Cần cấu hình `JAVA_HOME` ổn định cho terminal/dev machine để build bằng lệnh chuẩn mà không cần set Android Studio JBR tạm.

#### Medium

- Rooms list đang dùng XML card tĩnh, chưa có RecyclerView/adapter.
- Dữ liệu phòng/detail/form đang là mock/static.
- Add/Edit Room chưa lưu dữ liệu thật.
- Meter-reading action chưa có route riêng nên đang disabled.
- Chưa có loading/empty/error state thật từ backend.

#### Low

- `app_name` vẫn là `Rent`.
- Room card/status có thể polish thêm icon/thumbnail khi có asset thật.

### 12. Kết luận

**READY_FOR_PHASE_2D**

- `assembleDebug` build thành công khi dùng JBR hợp lệ.
- Rooms UI hiển thị được bằng XML.
- Room Detail UI hiển thị được bằng XML.
- Add/Edit Room UI hiển thị được bằng XML.
- Navigation Rooms không lỗi build.
- Bottom navigation không bị thay đổi.
- Không gọi API thật.
- Không vi phạm XML + Kotlin stack.
- Không phá Auth/Home UI/design system.

## Phase 2D - Tenants UI

### 1. Mục tiêu phase

Phase 2D dựng UI thật cho module quản lý người thuê bằng Android Native XML + Kotlin. Phạm vi gồm Tenants list, tenant card, Tenant Detail, Add/Edit Tenant, status/filter UI và navigation nội bộ Tenants.

Không kết nối API, không tạo Retrofit/Room/repository, không lưu dữ liệu thật, không xử lý nghiệp vụ thật, không dựng Bills/Profile detail, không migrate Compose và không dùng web tech.

### 2. File đã tạo/sửa

- Tạo `app/src/main/res/layout/fragment_add_edit_tenant.xml`: form thêm/cập nhật người thuê bằng Material TextInputLayout.
- Tạo `app/src/main/java/com/example/rent/presentation/tenants/AddEditTenantFragment.kt`: Fragment form dùng ViewBinding, validation nhẹ và popBackStack.
- Sửa `app/src/main/res/layout/fragment_tenants.xml`: thay skeleton bằng list UI thật, summary cards, search/filter, tenant cards và empty state.
- Sửa `app/src/main/res/layout/fragment_tenant_detail.xml`: thay skeleton bằng detail UI gồm tenant info, room summary, billing summary và actions.
- Sửa `app/src/main/java/com/example/rent/presentation/tenants/TenantsFragment.kt`: thêm click listeners list -> detail và list -> add tenant.
- Sửa `app/src/main/java/com/example/rent/presentation/tenants/TenantDetailFragment.kt`: thêm click listeners edit, room, bills và disable action chưa có workflow thật.
- Sửa `app/src/main/res/navigation/main_nav_graph.xml`: thêm route/action cho tenant detail và add/edit tenant.
- Sửa `app/src/main/java/com/example/rent/navigation/AppRoutes.kt`: thêm route constant `ADD_EDIT_TENANT`.
- Sửa `app/src/main/res/values/strings.xml`: thêm strings cho Tenants UI, detail, form và validation.

### 3. Design source đã áp dụng

- Đã đọc `docs/FE_Architecture.md`.
- Đã đọc `docs/SMARTRENT_MOBILE_UI_GUIDE.md`.
- Đã đọc SKILL.md / Taste Skill (`design-taste-frontend`) và chỉ áp dụng phần phù hợp về spacing, hierarchy, scanability và polish.
- Đã đọc `docs/phase0_frontend_audit.md`.
- Đã đọc `docs/frontend_phase_change_log.md`.

Ưu tiên áp dụng: Android Native XML + Kotlin, Fragment + ViewBinding, Navigation Component, design tokens/styles từ Phase 1B và pattern tĩnh đã dùng ở Phase 2C. Không dùng Compose, Tailwind, React hoặc web CSS.

### 4. Tenants UI đã dựng

- Tenants list:
  - Header “Người thuê”.
  - Subtitle quản lý theo phòng, hợp đồng và công nợ.
  - Action thêm người thuê.
- Summary cards:
  - Tổng người thuê.
  - Đang thuê.
  - Sắp hết hạn.
  - Còn nợ.
- Search/filter area:
  - TextInputLayout tìm theo tên, số điện thoại hoặc phòng.
  - Filter chips: Tất cả, Đang thuê, Sắp hết hạn, Còn nợ, Đã rời đi.
- Tenant item/card:
  - Nguyễn Minh Anh, Trần Lan Hương, Lê Quốc Bảo bằng mock/static data.
  - Mỗi card có tên, số điện thoại, phòng, trạng thái thuê, thông tin hợp đồng/thanh toán.
  - Card chính có CTA xem chi tiết.
- Empty state:
  - Có `tenantsEmptyStateCard` đang `gone`, chuẩn bị cho trạng thái list rỗng.
- Resource/style đã dùng:
  - `bg_app`, `bg_chip`.
  - `Widget.SmartRent.Card`, `Widget.SmartRent.Button.*`, `Widget.SmartRent.TextInputLayout`, `Widget.SmartRent.TextInputEditText`.
  - `TextAppearance.SmartRent.*`, `sr_*` colors và `space_*` dimens.

Không dùng RecyclerView trong Phase 2D để tránh thêm adapter/model foundation ngoài phạm vi; list hiện là XML card tĩnh phục vụ UI foundation giống Phase 2C.

### 5. Tenant Detail UI

- Header/status:
  - Tên người thuê mock `Nguyễn Minh Anh`.
  - Status chip `Đang thuê`.
  - Action sửa.
- Tenant information:
  - Họ tên, số điện thoại, email, CCCD dạng che một phần, ngày bắt đầu thuê và ghi chú.
- Room summary:
  - Phòng 101, giá thuê, số người đang ở, trạng thái phòng.
  - CTA xem phòng trỏ sang route Room Detail đã có.
- Billing/payment summary:
  - Hóa đơn tháng 06/2026.
  - Trạng thái chờ thu.
  - Số tiền còn phải thu.
  - CTA xem hóa đơn trỏ sang Bills route hiện có.
- Actions đã chuẩn bị:
  - Sửa thông tin.
  - Chuyển phòng, đang disabled vì chưa có workflow thật.
  - Tạo hóa đơn trỏ sang Bills route.
  - Kết thúc thuê, đang disabled vì chưa có workflow thật.

### 6. Add/Edit Tenant UI

- Form fields:
  - Họ tên.
  - Số điện thoại.
  - Email.
  - CCCD/CMND.
  - Phòng đang thuê placeholder.
  - Ngày bắt đầu thuê placeholder.
  - Ghi chú.
- Button/actions:
  - Lưu thông tin.
  - Hủy.
- Validation nhẹ:
  - Họ tên không được để trống.
  - Số điện thoại không được để trống.
  - Phòng không được để trống.
  - Validation chỉ hiển thị lỗi UI, không lưu dữ liệu thật.

### 7. Navigation Tenants

- Routes đã thêm/sửa:
  - `tenantsFragment`.
  - `tenantDetailFragment`.
  - `addEditTenantFragment`.
- Actions đã thêm/sửa:
  - `action_tenants_to_tenant_detail`.
  - `action_tenants_to_add_tenant` với `formMode=add`.
  - `action_tenant_detail_to_edit_tenant` với `formMode=edit`.
  - `action_tenant_detail_to_room_detail`.
  - `action_tenant_detail_to_bills`.
- Bottom navigation status:
  - Tenants tab vẫn dùng `tenantsFragment`.
  - Không redesign bottom nav.
  - Không phá Auth/Home/Rooms navigation.
- Mock argument/navigation:
  - Chỉ dùng argument String `formMode` cho Add/Edit title.
  - Không thêm tenantId thật và không thêm deep link.

### 8. UI behavior

- Click listener đã thêm:
  - Tenants Add Tenant -> Add/Edit Tenant.
  - Empty Add Tenant -> Add/Edit Tenant.
  - Tenant cards -> Tenant Detail.
  - Tenant CTA -> Tenant Detail.
  - Tenant Detail Edit -> Add/Edit Tenant mode edit.
  - Tenant Detail View Room -> Room Detail.
  - Tenant Detail View Bill/Create Bill -> Bills.
- Dữ liệu đang là mock/static trong XML strings.
- Không gọi API, không tạo repository, không tạo Room DB, không lưu dữ liệu và không xử lý nghiệp vụ thật.

### 9. Resource/string/drawable status

- Strings đã thêm:
  - Tenants title/subtitle.
  - Summary labels/values.
  - Filter labels.
  - Tenant card labels/status/content.
  - Tenant Detail labels/content/actions.
  - Add/Edit Tenant form labels/actions/errors.
- Drawable/icon đã thêm:
  - Không thêm drawable/icon mới.
  - Dùng lại `bg_app` và `bg_chip` từ Phase 1B.
- Resource token đã dùng:
  - `sr_success`, `sr_success_container`, `sr_warning`, `sr_warning_container`, `sr_error`, `sr_error_container`, `sr_info_container`, `sr_primary`.
  - `space_4`, `space_8`, `space_12`, `space_16`, `space_20`, `space_24`, `space_32`.

### 10. Build status

Lệnh chuẩn đã chạy:

```powershell
.\gradlew.bat :app:assembleDebug
```

Kết quả:

- Fail do terminal chưa có `JAVA_HOME` và không tìm thấy `java` trong `PATH`.
- Đây là lỗi cấu hình môi trường local, không phải lỗi source Phase 2D.

Lệnh build kiểm chứng bằng Android Studio JBR tạm:

```powershell
$env:JAVA_HOME='C:\Program Files\Android\Android Studio\jbr'
$env:Path="$env:JAVA_HOME\bin;$env:Path"
.\gradlew.bat :app:assembleDebug
```

Kết quả:

- `BUILD SUCCESSFUL`.
- Các task quan trọng đã chạy qua: `processDebugNavigationResources`, `compileDebugNavigationResources`, `mergeDebugResources`, `dataBindingGenBaseClassesDebug`, `processDebugResources`, `compileDebugKotlin`, `compileDebugJavaWithJavac`, `packageDebug`, `assembleDebug`.

### 11. Vấn đề còn lại

#### Critical

- Không có lỗi source/build blocker khi dùng JBR hợp lệ.

#### High

- Cần cấu hình `JAVA_HOME` ổn định cho terminal/dev machine để build bằng lệnh chuẩn mà không cần set Android Studio JBR tạm.

#### Medium

- Tenants list đang dùng XML card tĩnh, chưa có RecyclerView/adapter.
- Dữ liệu người thuê/detail/form đang là mock/static.
- Add/Edit Tenant chưa lưu dữ liệu thật.
- Chuyển phòng và kết thúc thuê đang disabled vì chưa có workflow/nghiệp vụ thật.
- Chưa có loading/empty/error state thật từ backend.

#### Low

- `app_name` vẫn là `Rent`.
- Tenant cards có thể polish thêm avatar/icon khi có asset thật.

### 12. Kết luận

**READY_FOR_PHASE_2E**

- `assembleDebug` build thành công khi dùng JBR hợp lệ.
- Tenants UI hiển thị được bằng XML.
- Tenant Detail UI hiển thị được bằng XML.
- Add/Edit Tenant UI hiển thị được bằng XML.
- Navigation Tenants không lỗi build.
- Bottom navigation không bị thay đổi.
- Không gọi API thật.
- Không vi phạm XML + Kotlin stack.
- Không phá Auth/Home/Rooms UI/design system.

## Phase 2E - Bills / Payments / Meter Reading UI

### 1. Mục tiêu phase

Phase 2E dựng UI thật cho module hóa đơn, thanh toán và ghi chỉ số điện/nước bằng Android Native XML + Kotlin. Phạm vi gồm Bills list, Bill Detail, Create/Edit Bill, Meter Reading, Payment Record, payment status UI, navigation nội bộ Bills và mock/static data phục vụ hiển thị.

Không kết nối API, không tạo Retrofit/Room/repository, không lưu dữ liệu thật, không tính tiền/chốt hóa đơn/phát hành hóa đơn thật, không sửa backend, không dựng Profile/Settings detail, không migrate Compose và không dùng web tech.

### 2. File đã tạo/sửa

- Tạo `app/src/main/res/layout/fragment_add_edit_bill.xml`: form tạo/cập nhật hóa đơn bằng Material TextInputLayout.
- Tạo `app/src/main/res/layout/fragment_meter_reading.xml`: form ghi chỉ số điện/nước bằng Material TextInputLayout.
- Tạo `app/src/main/res/layout/fragment_payment_record.xml`: form ghi nhận thanh toán bằng Material TextInputLayout.
- Tạo `app/src/main/java/com/example/rent/presentation/bills/AddEditBillFragment.kt`: Fragment form hóa đơn dùng ViewBinding, validation nhẹ và popBackStack.
- Tạo `app/src/main/java/com/example/rent/presentation/bills/MeterReadingFragment.kt`: Fragment ghi chỉ số dùng ViewBinding, validation nhẹ và popBackStack.
- Tạo `app/src/main/java/com/example/rent/presentation/bills/PaymentRecordFragment.kt`: Fragment ghi nhận thanh toán dùng ViewBinding, validation nhẹ và popBackStack.
- Sửa `app/src/main/res/layout/fragment_bills.xml`: thay skeleton bằng Bills list UI thật, summary cards, filter chips, bill cards và empty state.
- Sửa `app/src/main/res/layout/fragment_bill_detail.xml`: thay skeleton bằng Bill Detail UI gồm room/tenant summary, charge breakdown, meter summary, payment summary và actions.
- Sửa `app/src/main/java/com/example/rent/presentation/bills/BillsFragment.kt`: thêm click listeners list -> detail và list -> create bill.
- Sửa `app/src/main/java/com/example/rent/presentation/bills/BillDetailFragment.kt`: thêm click listeners edit bill, meter reading, payment record và disable share/export placeholder.
- Sửa `app/src/main/res/navigation/main_nav_graph.xml`: thêm route/action cho create/edit bill, meter reading và payment record.
- Sửa `app/src/main/java/com/example/rent/navigation/AppRoutes.kt`: thêm route constants `ADD_EDIT_BILL`, `METER_READING`, `PAYMENT_RECORD`.
- Sửa `app/src/main/res/values/strings.xml`: thêm strings cho Bills UI, Bill Detail, Create/Edit Bill, Meter Reading, Payment Record và validation.

### 3. Design source đã áp dụng

- Đã đọc `docs/FE_Architecture.md`.
- Đã đọc `docs/SMARTRENT_MOBILE_UI_GUIDE.md`.
- Đã đọc SKILL.md / Taste Skill (`design-taste-frontend`) và chỉ áp dụng phần phù hợp về spacing, hierarchy, scanability và polish.
- Đã đọc `docs/phase0_frontend_audit.md`.
- Đã đọc `docs/frontend_phase_change_log.md`.

Ưu tiên áp dụng: Android Native XML + Kotlin, Fragment + ViewBinding, Navigation Component, design tokens/styles từ Phase 1B và pattern XML card tĩnh đã dùng ở Phase 2C/2D. Không dùng Compose, Tailwind, React hoặc web CSS.

### 4. Bills UI đã dựng

- Bills list:
  - Header “Hóa đơn”.
  - Subtitle theo dõi tiền phòng, điện nước và trạng thái thanh toán.
  - Action tạo hóa đơn.
- Summary cards:
  - Cần thu tháng này.
  - Đã thu.
  - Chờ thanh toán.
  - Quá hạn.
  - Số hóa đơn tháng này.
- Filter/status chips:
  - Tất cả.
  - Chờ thanh toán.
  - Đã thanh toán.
  - Quá hạn.
  - Nháp.
  - Tháng này.
- Bill item/card:
  - HD-0626-101, HD-0626-203, HD-0526-305 bằng mock/static data.
  - Mỗi card có mã hóa đơn, phòng, người thuê đại diện, kỳ hóa đơn, tổng tiền, trạng thái và hạn/ngày thanh toán.
  - Card chính có CTA xem chi tiết.
- Empty state:
  - Có `billsEmptyStateCard` đang `gone`, chuẩn bị cho trạng thái list rỗng.
- Resource/style đã dùng:
  - `bg_app`, `bg_chip`.
  - `Widget.SmartRent.Card`, `Widget.SmartRent.Button.*`, `Widget.SmartRent.TextInputLayout`, `Widget.SmartRent.TextInputEditText`.
  - `TextAppearance.SmartRent.*`, `sr_*` colors và `space_*` dimens.

Không dùng RecyclerView trong Phase 2E để tránh thêm adapter/model foundation ngoài phạm vi; list hiện là XML card tĩnh phục vụ UI foundation giống Phase 2C/2D.

### 5. Bill Detail UI

- Header/status:
  - Mã hóa đơn mock `HD-0626-101`.
  - Kỳ hóa đơn tháng 06/2026.
  - Status chip `Chờ thanh toán`.
  - Action sửa.
- Room/Tenant summary:
  - Phòng 101, Khu A, 2 người đang ở.
  - Người đại diện Nguyễn Minh Anh.
  - Ngày phát hành và hạn thanh toán mock.
- Charge breakdown:
  - Tiền phòng.
  - Tiền điện.
  - Tiền nước.
  - Phụ phí.
  - Giảm trừ.
  - Tổng cộng.
- Meter summary:
  - Chỉ số điện đầu/cuối kỳ và số điện sử dụng.
  - Chỉ số nước đầu/cuối kỳ và số nước sử dụng.
  - CTA ghi chỉ số trỏ sang Meter Reading route.
- Payment summary:
  - Đã thanh toán.
  - Còn lại.
  - Ghi chú thanh toán gần nhất.
  - CTA ghi nhận thanh toán trỏ sang Payment Record route.
- Actions đã chuẩn bị:
  - Ghi nhận thanh toán.
  - Ghi chỉ số điện/nước.
  - Sửa hóa đơn.
  - Chia sẻ hóa đơn đang disabled vì chưa có publish/export thật.

### 6. Create/Edit Bill UI

- Form fields:
  - Phòng placeholder.
  - Kỳ hóa đơn placeholder.
  - Tiền phòng.
  - Tiền điện.
  - Tiền nước.
  - Phụ phí.
  - Giảm trừ.
  - Hạn thanh toán placeholder.
  - Ghi chú.
- Button/actions:
  - Lưu hóa đơn.
  - Hủy.
- Validation nhẹ:
  - Phòng không được để trống.
  - Kỳ hóa đơn không được để trống.
  - Tiền phòng/tổng tiền không được để trống.
- Chưa tính tiền thật:
  - Không cộng/trừ các khoản bằng business logic.
  - Không phát hành hóa đơn thật.
  - Không lưu dữ liệu vào API/DB.

### 7. Meter Reading UI

- Field chỉ số điện/nước:
  - Phòng.
  - Kỳ ghi chỉ số.
  - Chỉ số điện đầu kỳ/cuối kỳ.
  - Chỉ số nước đầu kỳ/cuối kỳ.
  - Ghi chú.
- Preview sử dụng:
  - Có `usagePreviewTextView` dạng placeholder hướng dẫn nhập chỉ số cuối kỳ.
  - Không tính tiền billing thật.
- Validation nhẹ:
  - Chỉ số điện cuối kỳ không được để trống.
  - Chỉ số nước cuối kỳ không được để trống.
- Chưa lưu dữ liệu thật:
  - Save chỉ popBackStack khi validation pass.
  - Không gọi API, không ghi DB, không update hóa đơn thật.

### 8. Payment Record UI

- Field thanh toán:
  - Hóa đơn/phòng đang thanh toán.
  - Tổng tiền, đã thanh toán, còn lại.
  - Số tiền thanh toán.
  - Phương thức thanh toán placeholder.
  - Ngày thanh toán placeholder.
  - Ghi chú.
- Payment method placeholder:
  - Hiển thị `Chuyển khoản`; string `Tiền mặt` cũng đã chuẩn bị.
- Validation nhẹ:
  - Số tiền không được để trống.
  - Số tiền phải lớn hơn 0 với parse số đơn giản.
- Chưa lưu thanh toán thật:
  - Save chỉ popBackStack khi validation pass.
  - Không gọi API, không cập nhật hóa đơn thật, không ghi lịch sử thanh toán thật.

### 9. Navigation Bills

- Routes đã thêm/sửa:
  - `billsFragment`.
  - `billDetailFragment`.
  - `addEditBillFragment`.
  - `meterReadingFragment`.
  - `paymentRecordFragment`.
- Actions đã thêm/sửa:
  - `action_bills_to_bill_detail`.
  - `action_bills_to_create_bill` với `formMode=add`.
  - `action_bill_detail_to_edit_bill` với `formMode=edit`.
  - `action_bill_detail_to_meter_reading`.
  - `action_bill_detail_to_payment_record`.
- Bottom navigation status:
  - Bills tab vẫn dùng `billsFragment`.
  - Không redesign bottom nav.
  - Không phá Auth/Home/Rooms/Tenants navigation.
- Mock argument/navigation:
  - Chỉ dùng argument String `formMode` cho Add/Edit Bill title.
  - Không thêm billId thật và không thêm deep link.

### 10. UI behavior

- Click listener đã thêm:
  - Bills Create Bill -> Add/Edit Bill mode add.
  - Empty Create Bill -> Add/Edit Bill mode add.
  - Bill cards -> Bill Detail.
  - Bill CTA -> Bill Detail.
  - Bill Detail Edit -> Add/Edit Bill mode edit.
  - Bill Detail Meter Reading -> Meter Reading.
  - Bill Detail Record Payment/Primary Payment -> Payment Record.
- Dữ liệu đang là mock/static trong XML strings.
- Không gọi API, không tạo repository, không tạo Room DB, không lưu dữ liệu, không tính tiền/chốt hóa đơn/phát hành hóa đơn thật.

### 11. Resource/string/drawable status

- Strings đã thêm:
  - Bills title/subtitle.
  - Summary labels/values.
  - Filter labels.
  - Bill card labels/status/content.
  - Bill Detail labels/content/actions.
  - Create/Edit Bill form labels/actions/errors.
  - Meter Reading labels/actions/errors.
  - Payment Record labels/actions/errors.
- Drawable/icon đã thêm:
  - Không thêm drawable/icon mới.
  - Dùng lại `bg_app` và `bg_chip` từ Phase 1B.
- Resource token đã dùng:
  - `sr_info_container`, `sr_primary`, `sr_success`, `sr_success_container`, `sr_warning`, `sr_warning_container`, `sr_error`, `sr_error_container`, `sr_neutral_container`.
  - `space_4`, `space_8`, `space_12`, `space_16`, `space_20`, `space_24`, `space_32`.

### 12. Build status

Lệnh chuẩn đã chạy:

```powershell
.\gradlew.bat :app:assembleDebug
```

Kết quả:

- Fail do terminal chưa có `JAVA_HOME` và không tìm thấy `java` trong `PATH`.
- Đây là lỗi cấu hình môi trường local, không phải lỗi source Phase 2E.

Lệnh build kiểm chứng bằng Android Studio JBR tạm:

```powershell
$env:JAVA_HOME='C:\Program Files\Android\Android Studio\jbr'
$env:Path="$env:JAVA_HOME\bin;$env:Path"
.\gradlew.bat :app:assembleDebug
```

Kết quả:

- `BUILD SUCCESSFUL`.
- Các task quan trọng đã chạy qua: `processDebugNavigationResources`, `compileDebugNavigationResources`, `mergeDebugResources`, `dataBindingGenBaseClassesDebug`, `processDebugResources`, `compileDebugKotlin`, `compileDebugJavaWithJavac`, `packageDebug`, `assembleDebug`.

### 13. Vấn đề còn lại

#### Critical

- Không có lỗi source/build blocker khi dùng JBR hợp lệ.

#### High

- Cần cấu hình `JAVA_HOME` ổn định cho terminal/dev machine để build bằng lệnh chuẩn mà không cần set Android Studio JBR tạm.

#### Medium

- Bills list đang dùng XML card tĩnh, chưa có RecyclerView/adapter.
- Dữ liệu hóa đơn, chỉ số và thanh toán đang là mock/static.
- Create/Edit Bill chưa tính tiền, chưa phát hành và chưa lưu hóa đơn thật.
- Meter Reading chưa lưu dữ liệu thật và preview chỉ là placeholder.
- Payment Record chưa lưu thanh toán thật và chưa cập nhật trạng thái hóa đơn.
- Chia sẻ/xuất hóa đơn đang disabled vì chưa có publish/export workflow.

#### Low

- `app_name` vẫn là `Rent`.
- Bill cards có thể polish thêm icon/thumbnail khi có asset thật.

### 14. Kết luận

**READY_FOR_PHASE_2F**

- `assembleDebug` build thành công khi dùng JBR hợp lệ.
- Bills UI hiển thị được bằng XML.
- Bill Detail UI hiển thị được bằng XML.
- Create/Edit Bill UI hiển thị được bằng XML.
- Meter Reading UI hiển thị được bằng XML.
- Payment Record UI hiển thị được bằng XML.
- Navigation Bills không lỗi build.
- Bottom navigation không bị thay đổi.
- Không gọi API thật.
- Không tính/chốt hóa đơn thật.
- Không vi phạm XML + Kotlin stack.
- Không phá Auth/Home/Rooms/Tenants UI/design system.

## Phase 2F - Profile / Settings / Notifications UI

### 1. Muc tieu phase

Phase 2F dung UI tinh cho Profile, Settings, Notifications va Notification Detail bang Android Native XML + Kotlin. Pham vi chi gom mock/static data, ViewBinding lifecycle va navigation noi bo giua Profile, Settings, Notifications, Notification Detail, Bills va Rooms.

Khong ket noi API, khong tao Retrofit/Room/repository, khong luu settings that, khong push notification that, khong logout that, khong sua backend/nghiep vu, khong migrate Compose va khong tao design system moi.

### 2. File da tao/sua

- Sua `app/src/main/res/layout/fragment_profile.xml`: thay skeleton bang Profile UI tinh, account card, summary cards, menu actions va app info.
- Tao `app/src/main/res/layout/fragment_settings.xml`: Settings UI tinh gom account, space, preferences, notification switches va session actions disabled.
- Sua `app/src/main/res/layout/fragment_notifications.xml`: thay skeleton bang notification list UI tinh, filter chips, unread/read cards va CTA xem chi tiet.
- Tao `app/src/main/res/layout/fragment_notification_detail.xml`: Notification Detail UI tinh gom noi dung, doi tuong lien quan va actions sang Bills/Rooms.
- Sua `app/src/main/java/com/example/rent/presentation/profile/ProfileFragment.kt`: them navigation Profile -> Settings va Profile -> Notifications, giu edit/logout disabled.
- Tao `app/src/main/java/com/example/rent/presentation/settings/SettingsFragment.kt`: Fragment ViewBinding cho Settings, khong persistence.
- Sua `app/src/main/java/com/example/rent/presentation/notifications/NotificationsFragment.kt`: them click listeners vao Notification Detail, giu mark-all-read disabled.
- Tao `app/src/main/java/com/example/rent/presentation/notifications/NotificationDetailFragment.kt`: Fragment ViewBinding cho detail, navigation sang Bills/Rooms, mark-read disabled.
- Sua `app/src/main/res/navigation/main_nav_graph.xml`: them `settingsFragment`, `notificationDetailFragment` va actions lien quan.
- Sua `app/src/main/java/com/example/rent/navigation/AppRoutes.kt`: them `SETTINGS` va `NOTIFICATION_DETAIL`.
- Sua `app/src/main/res/values/strings.xml`: them strings cho Profile, Settings, Notifications va Notification Detail.

### 3. UI behavior

- Profile hien thi thong tin chu nha mock, summary tai khoan va menu actions; Settings/Notifications co navigation noi bo; Edit Profile va Logout disabled.
- Settings hien thi account/space/preferences/notification options; switch chi doi trang thai local cua UI, khong persist; Logout/Delete account disabled.
- Notifications hien thi danh sach thong bao mock; notification card va CTA mo Notification Detail; Mark all read disabled.
- Notification Detail hien thi noi dung mock, co navigation sang Bills va Rooms; Mark read disabled.

### 4. Build status

Lenh chuan da chay:

```powershell
.\gradlew.bat :app:assembleDebug
```

Ket qua:

- Fail do terminal chua co `JAVA_HOME` va khong tim thay `java` trong `PATH`.
- Day la loi moi truong local, khong phai loi source Phase 2F.

Lenh kiem chung bang Android Studio JBR tam:

```powershell
$env:JAVA_HOME='C:\Program Files\Android\Android Studio\jbr'
$env:Path="$env:JAVA_HOME\bin;$env:Path"
.\gradlew.bat :app:assembleDebug
```

Ket qua:

- `BUILD SUCCESSFUL`.
- Cac task quan trong da qua: `processDebugNavigationResources`, `compileDebugNavigationResources`, `mergeDebugResources`, `dataBindingGenBaseClassesDebug`, `processDebugResources`, `compileDebugKotlin`, `compileDebugJavaWithJavac`, `packageDebug`, `assembleDebug`.

### 5. Van de con lai

#### Critical

- Khong co loi source/build blocker khi dung JBR hop le.

#### High

- Can cau hinh `JAVA_HOME` on dinh cho terminal/dev machine de build bang lenh chuan ma khong can set Android Studio JBR tam.

#### Medium

- Profile/Settings/Notifications dang dung mock/static data.
- Settings switches chua persist.
- Notification read state, push notification va deep link that chua duoc trien khai.
- Logout/delete account/edit profile la placeholder disabled.

#### Low

- Chuoi Phase 2F moi them dang uu tien ASCII de tranh lech encoding hien co trong file resource/doc khi doc bang PowerShell.
- Co the polish them icon/avatar asset that o phase sau neu co bo asset chinh thuc.

### 6. Ket luan

**READY_FOR_PHASE_3A**

- `assembleDebug` build thanh cong khi dung JBR hop le.
- Profile UI hien thi duoc bang XML.
- Settings UI hien thi duoc bang XML.
- Notifications UI hien thi duoc bang XML.
- Notification Detail UI hien thi duoc bang XML.
- Navigation noi bo Profile/Settings/Notifications khong loi build.
- Bottom navigation khong bi thay doi.
- Khong goi API that.
- Khong persist settings, khong logout that, khong push notification that.
- Khong vi pham XML + Kotlin stack.
- Khong pha Auth/Home/Rooms/Tenants/Bills UI/design system.

## Phase 3A - UI State + Mock Data Standardization

### 1. Muc tieu phase

Phase 3A chuan hoa foundation cho mock data, UiModel va UiState cua cac man da dung tu Phase 2A den Phase 2F. Phase nay chi tao mock/demo data, state rendering nhe va helper hien thi; khong ket noi API, khong tao Retrofit/Room/repository, khong luu du lieu that, khong tinh tien that, khong redesign UI va khong migrate Compose.

### 2. File da tao/sua

- Tao `app/src/main/java/com/example/rent/core/common/UiState.kt`: UiState chung gom Loading, Success, Empty, Error.
- Tao `app/src/main/java/com/example/rent/core/mock/SmartRentMockData.kt`: mock/demo data tap trung cho Home, Rooms, Tenants, Bills, Meter Reading, Payment, Profile va Notifications.
- Tao `app/src/main/java/com/example/rent/core/utils/DisplayFormatters.kt`: helper hien thi nhe cho currency string, room capacity va room code; khong xu ly nghiep vu that.
- Tao `app/src/main/java/com/example/rent/presentation/model/SmartRentUiModels.kt`: UiModel cho dashboard, room, tenant, bill, meter, payment, profile va notification.
- Sua `app/src/main/java/com/example/rent/presentation/home/HomeFragment.kt`: them state mac dinh `Success(mockData)`, renderLoading/renderContent/renderEmpty/renderError.
- Sua `app/src/main/java/com/example/rent/presentation/rooms/RoomsFragment.kt`: them UiState render pattern va toggle room cards/empty state theo mock data.
- Sua `app/src/main/java/com/example/rent/presentation/tenants/TenantsFragment.kt`: them UiState render pattern va toggle tenant cards/empty state theo mock data.
- Sua `app/src/main/java/com/example/rent/presentation/bills/BillsFragment.kt`: them UiState render pattern va toggle bill cards/empty state theo mock data.
- Sua `app/src/main/java/com/example/rent/presentation/notifications/NotificationsFragment.kt`: them UiState render pattern va toggle notification cards theo mock data; mark-all-read van disabled.
- Sua `app/src/main/java/com/example/rent/presentation/profile/ProfileFragment.kt`: them UiState foundation va mock profile source, khong them persistence.

### 3. Design source da ap dung

- Da doc `docs/FE_Architecture.md`.
- Da doc `docs/SMARTRENT_MOBILE_UI_GUIDE.md`.
- Da doc SKILL.md / Taste Skill (`design-taste-frontend`) va chi ap dung phan phu hop voi hierarchy, spacing, state quality va native mobile product UI.
- Da doc `docs/phase0_frontend_audit.md`.
- Da doc `docs/frontend_phase_change_log.md` va xac nhan Phase 2F dang `READY_FOR_PHASE_3A`.

### 4. Mock data da chuan hoa

- Mock data tap trung tai `com.example.rent.core.mock.SmartRentMockData`.
- Module da co mock source chuan: Home, Rooms, Tenants, Bills, Meter Reading, Payment Record, Profile, Notifications.
- Mock data chi phuc vu UI demo, khong nam trong `data.repository`, khong tao API gia, khong tao database gia.
- Cac layout XML van con text demo trong `strings.xml` de giu UI tinh da build on dinh tu Phase 2A-2F. Chua thay the toan bo binding text sang Kotlin de tranh refactor lon va tranh pha layout hien co.

### 5. UiModel da chuan hoa

- Da tao `HomeOverviewUiModel`, `HomeQuickActionUiModel`, `RecentActivityUiModel`.
- Da tao `RoomUiModel`, `RoomStatus`.
- Da tao `TenantUiModel`, `TenantStatus`.
- Da tao `BillUiModel`, `BillStatus`, `MeterReadingUiModel`, `PaymentUiModel`.
- Da tao `ProfileUiModel`, `ProfileSummaryUiModel`.
- Da tao `NotificationUiModel`, `NotificationType`.
- Hien chua co Adapter trong project, nen khong co adapter nao can chuyen sang UiModel.

### 6. UiState/state rendering

- Da tao UiState chung tai `core.common.UiState`.
- `HomeFragment` co renderLoading/renderContent/renderEmpty/renderError foundation.
- `RoomsFragment` co renderLoading/renderContent/renderEmpty/renderError va dung `roomsEmptyStateCard` san co.
- `TenantsFragment` co renderLoading/renderContent/renderEmpty/renderError va dung `tenantsEmptyStateCard` san co.
- `BillsFragment` co renderLoading/renderContent/renderEmpty/renderError va dung `billsEmptyStateCard` san co.
- `NotificationsFragment` co renderLoading/renderContent/renderEmpty/renderError va toggle notification cards.
- `ProfileFragment` co UiState foundation nhe cho profile content.
- Khong tao layout loading/error rieng trong phase nay vi UI hien tai la XML card tinh; render functions da tao foundation ma khong redesign man hinh.

### 7. Adapter/list cleanup

- Khong phat hien `RoomAdapter`, `TenantAdapter`, `BillAdapter`, `NotificationAdapter` hay RecyclerView trong source hien tai.
- Khong co adapter nao dang tu tao mock data.
- Click listener/navigation van nam trong Fragment, phu hop voi setup hien tai va khong goi API.
- Cac list hien van la XML card tinh; mock UiModel da san sang de chuyen sang adapter/RecyclerView o phase sau neu duoc yeu cau.

### 8. Formatter/helper status

- Da tao `DisplayFormatters` tai `core.utils`.
- Helper chi format hien thi don gian: currency string passthrough, room capacity text, room code uppercase.
- Khong xu ly locale phuc tap, khong tinh tien that, khong dua business/backend logic vao helper.

### 9. Build status

Lenh chuan da chay:

```powershell
.\gradlew.bat :app:assembleDebug
```

Ket qua:

- Fail do terminal chua co `JAVA_HOME` va khong tim thay `java` trong `PATH`.
- Day la loi moi truong local, khong phai loi source Phase 3A.

Lenh kiem chung bang Android Studio JBR tam:

```powershell
$env:JAVA_HOME='C:\Program Files\Android\Android Studio\jbr'
$env:Path="$env:JAVA_HOME\bin;$env:Path"
.\gradlew.bat :app:assembleDebug
```

Ket qua:

- `BUILD SUCCESSFUL`.
- Cac task quan trong da qua: `processDebugResources`, `compileDebugKotlin`, `compileDebugJavaWithJavac`, `packageDebug`, `assembleDebug`.

### 10. Van de con lai

#### Critical

- Khong co loi source/build blocker khi dung JBR hop le.

#### High

- Can cau hinh `JAVA_HOME` on dinh cho terminal/dev machine de build bang lenh chuan ma khong can set Android Studio JBR tam.

#### Medium

- Text mock hien thi tren UI van chu yeu nam trong `strings.xml`/XML static card de tranh refactor lon trong Phase 3A.
- Loading/error state moi la render foundation; chua co layout loading/error rieng.
- Cac list chua co RecyclerView/Adapter nen UiModel chua render thanh item dong.

#### Low

- Mock data moi them dung ASCII/transliteration de tranh lech encoding da ton tai trong mot so file khi doc bang PowerShell.
- Co the bo sung empty/error/loading include layout dep hon trong phase sau neu bat dau chuyen list sang dynamic rendering.

### 11. Ket luan

**READY_FOR_PHASE_3B**

- `assembleDebug` build thanh cong khi dung JBR hop le.
- Mock data da co provider tap trung, khong rai trong adapter/fragment o cac man chinh.
- UiModel cho cac list/card chinh da ro rang.
- Cac man list chinh da co foundation render loading/content/empty/error.
- Khong goi API that.
- Khong tao repository/backend logic that.
- Khong vi pham XML + Kotlin stack.
- Khong pha UI/navigation/design system da lam.

## Phase 3B - UI Interaction Polish + Form Validation Standardization

### 1. Muc tieu phase

Phase 3B chuan hoa validation UI/client-side, TextInputLayout error, Snackbar feedback, keyboard behavior va click/back behavior cho cac form da dung tu Phase 2A den Phase 2F. Phase nay khong ket noi API, khong tao repository/database/token storage, khong tinh tien hay luu du lieu that, khong redesign UI va khong migrate Compose.

### 2. File da tao/sua

- Tao `app/src/main/java/com/example/rent/core/utils/FormValidators.kt`: helper validation dung chung cho required, email, phone, password, amount/number va meter reading.
- Tao `app/src/main/java/com/example/rent/core/ui/UiFeedback.kt`: helper Snackbar, short message, hide keyboard va clear error khi nguoi dung nhap lai.
- Sua Auth fragments: `LoginFragment.kt`, `RegisterFragment.kt`, `ForgotPasswordFragment.kt` de dung helper, clear error on text change va feedback mock.
- Sua form fragments: `AddEditRoomFragment.kt`, `AddEditTenantFragment.kt`, `AddEditBillFragment.kt`, `MeterReadingFragment.kt`, `PaymentRecordFragment.kt` de chuan hoa validation, keyboard hide va mock save feedback.
- Sua XML form layouts: `fragment_login.xml`, `fragment_register.xml`, `fragment_forgot_password.xml`, `fragment_add_edit_room.xml`, `fragment_add_edit_tenant.xml`, `fragment_add_edit_bill.xml`, `fragment_meter_reading.xml`, `fragment_payment_record.xml` de bo sung singleLine, numberDecimal, imeOptions va maxLines phu hop.
- Sua `app/src/main/res/values/strings.xml`: them error/message string dung chung cho Phase 3B.
- Sua `docs/frontend_phase_change_log.md`: append log duy nhat cua phase.

### 3. Design source da ap dung

- Da doc `docs/FE_Architecture.md`.
- Da doc `docs/SMARTRENT_MOBILE_UI_GUIDE.md`.
- Da doc SKILL.md / Taste Skill va chi ap dung nhu rule phu ve hierarchy, spacing, feedback clarity va polish; uu tien design guide cua project.
- Da doc `docs/phase0_frontend_audit.md`.
- Da doc `docs/frontend_phase_change_log.md` va xac nhan Phase 3A dang `READY_FOR_PHASE_3B`.

### 4. Validation da chuan hoa

- Helper validation: `FormValidators` trong `core.utils`.
- Auth: Login required contact/password, contact email-or-phone; Register required full name/contact/password/confirm, password min 6 ky tu, confirm match; Forgot Password required va valid contact.
- Room: required room name, rent price positive, capacity required positive, area optional positive number.
- Tenant: required name, required valid phone, optional valid email, required room.
- Bill: required room, period, rent amount positive, due date; optional electric/water/fee/discount non-negative number.
- Meter Reading: required start/end readings, valid numbers, end reading >= start reading, usage preview UI only.
- Payment: required positive payment amount, required method va payment date placeholder.

### 5. UI feedback/click behavior

- Da tao `UiFeedback` dung Material Snackbar va hide keyboard.
- Save buttons tren Room/Tenant/Bill/Meter/Payment hien Snackbar mock va quay lai man truoc sau delay ngan.
- Forgot Password hien Snackbar mock "da gui huong dan khoi phuc" va khong goi API.
- Login van la mock action navigate sang Home sau khi validate hop le.
- Cancel/back form van dung `popBackStack()` theo flow hien tai.
- Cac action van la mock/pending, khong luu du lieu that.

### 6. Keyboard/input behavior

- Auth contact/password/name fields duoc dat `singleLine` va imeOptions Next/Done.
- Room/Tenant/Bill/Payment amount fields dung `numberDecimal` khi phu hop.
- Meter reading fields dung `numberDecimal`, `singleLine` va imeOptions Next.
- Note fields duoc gioi han `minLines=3`, `maxLines=5`.

### 7. Back navigation/status

- Room, Tenant, Bill, Meter Reading va Payment save/cancel tiep tuc quay lai man truoc bang `popBackStack()`.
- Register/back-to-login va Forgot/back-to-login tiep tuc dung fallback navigation hien co.
- Khong thay doi navigation graph.

### 8. Resource/string status

- Da them string dung chung: `error_required_field`, `error_invalid_email`, `error_invalid_phone`, `error_invalid_contact`, `error_password_min_length`, `error_password_mismatch`, `error_invalid_amount`, `error_invalid_number`, `error_required_positive_number`, `error_meter_start_required`, `error_meter_reading_invalid`, `message_mock_save_success`, `message_mock_login_success`, `message_mock_reset_sent`, `message_feature_pending`, `meter_usage_preview_value`.
- Error text trong cac form Phase 3B dung resource string, khong them hardcode text dai trong Kotlin.

### 9. Build status

Lenh build da chay:

```powershell
.\gradlew.bat :app:assembleDebug
```

Ket qua:

- `BUILD SUCCESSFUL`.
- Khong can dung `JAVA_HOME` tam trong lan build Phase 3B nay.

### 10. Van de con lai

#### Critical

- Khong co.

#### High

- Khong co loi build/source blocker sau Phase 3B.

#### Medium

- Cac form van dung mock/static data va chua co persistence, dung pham vi Phase 3B.
- Save success Snackbar tren mot so form chi hien ngan truoc khi quay lai man truoc.

#### Low

- Cac field chon phong/ngay/phuong thuc van la placeholder disabled; bottom sheet/date picker that nen de phase sau khi co yeu cau.

### 11. Ket luan

**READY_FOR_PHASE_3C**

- `assembleDebug` build thanh cong.
- Cac form chinh co validation UI co ban va TextInputLayout error.
- Button/click/back behavior khong loi build.
- Khong goi API that.
- Khong tao repository/backend/database/storage logic that.
- Khong vi pham Android Native XML + Kotlin stack.
- Khong pha UI/navigation/design system da lam.

## Phase 3C - Responsive Polish + Accessibility + Final UI Audit Before API

### 1. Muc tieu phase

Phase 3C tap trung vao polish cuoi truoc khi sang API: responsive layout tren man nho/lon, spacing, typography, card hierarchy, visual consistency, accessibility co ban, touch target, empty/loading/error visual foundation, nav/back audit va resource cleanup. Phase nay khong ket noi API, khong tao Retrofit/repository/Room/DataStore/token storage, khong them business logic that, khong redesign lai app va khong migrate Compose.

### 2. File da tao/sua

- Sua `app/src/main/res/layout/fragment_rooms.xml`: them accessible card labels, ripple foreground va minHeight cho cac room cards co dieu huong.
- Sua `app/src/main/res/layout/fragment_tenants.xml`: them accessible card labels, ripple foreground va minHeight cho cac tenant cards co dieu huong.
- Sua `app/src/main/res/layout/fragment_bills.xml`: them accessible card labels, ripple foreground va minHeight cho cac bill cards co dieu huong.
- Sua `app/src/main/res/layout/fragment_notifications.xml`: khai bao clickable/focusable, accessible labels, ripple foreground va minHeight cho cac notification cards co dieu huong.
- Sua `app/src/main/res/layout/fragment_profile.xml`: chuan hoa 2 profile menu rows co dieu huong bang clickable/focusable, ripple foreground, center_vertical va 48dp minHeight san co.
- Sua `docs/frontend_phase_change_log.md`: append log duy nhat cua Phase 3C.

### 3. Design source da ap dung

- Canonical design file: `docs/SMARTRENT_MOBILE_UI_GUIDE.md`.
- Canonical architecture file: `docs/FE_Architecture.md`.
- Da doc `docs/phase0_frontend_audit.md`.
- Da doc SKILL.md / Taste Skill, chi dung nhu lens phu ve hierarchy, spacing, consistency va accessibility; design guide Android Native XML cua project van la source chinh.
- Da doc `docs/frontend_phase_change_log.md` va xac nhan Phase 3B dang `READY_FOR_PHASE_3C`.

### 4. Responsive layout audit

- Cac man chinh tiep tuc dung `NestedScrollView`, `fillViewport`, token spacing va width `match_parent`/weight thay vi fixed width.
- Khong them layout fixed pixel, khong them web/CSS/Tailwind pattern.
- Cac cards duoc giu trong vertical list de an toan tren man nho, cac filter ngang van dung `HorizontalScrollView`.
- Khong thay doi structure lon cua Home/List/Detail/Form de tranh regression truoc API.

### 5. Spacing, typography va hierarchy

- Giu lai token spacing va text appearance trong `styles.xml`/`dimens.xml`.
- Khong them palette/theme moi, khong lam redesign.
- Interactive card hierarchy duoc lam ro bang press feedback dong nhat thay vi chi co static card.
- Profile menu rows co gravity center_vertical de noi dung nam dung tam trong touch target 48dp.

### 6. Accessibility co ban

- Room/Tenant/Bill/Notification cards co dieu huong nay co `contentDescription` dua tren title/name chinh.
- Notification cards duoc khai bao `clickable` va `focusable` tai XML de accessibility tree ro rang hon, khong chi phu thuoc runtime listener.
- Profile settings/notifications rows duoc khai bao `clickable` va `focusable` tai XML.
- Khong them hidden text hoac behavior doc man hinh phuc tap khi chua co dynamic data.

### 7. Touch target va interaction feedback

- Cac card dieu huong trong Rooms/Tenants/Bills/Notifications duoc them `android:minHeight="@dimen/card_min_height"`.
- Cac card/rows co dieu huong duoc them `android:foreground="?attr/selectableItemBackground"` de co ripple/press feedback.
- Button styles hien co van giu `button_height=48dp`.
- Khong thay doi click destination hoac tao action moi.

### 8. Empty, loading va error state polish

- Cac fragment list van giu render foundation `UiState.Loading`, `Success`, `Empty`, `Error`.
- Empty state cards hien co van dung card compact, title/body/action va token spacing.
- Phase 3C khong tao dynamic loading/error layout rieng vi chua co API/async data thuc.
- Error/loading foundation duoc giu de Phase 4 API co the gan du lieu that ma khong doi architecture UI.

### 9. Navigation va back audit

- Da audit `main_nav_graph.xml` va cac `findNavController().navigate(...)` dang dung action id ton tai.
- Khong thay doi navigation graph.
- Back/cancel/save mock flows van dung `popBackStack()` hoac action fallback hien co.
- Bottom navigation va profile/notification routes giu nguyen.

### 10. Resource va architecture cleanup

- Khong them resource mau/dimen moi khong can thiet; tan dung `card_min_height`, `button_height`, token spacing va text styles san co.
- Khong tao repository, API service, Retrofit client, Room database, DataStore/token storage.
- Scan forbidden scope khong thay Retrofit, RoomDatabase, DataStore, OkHttp, ApiService, Repository, authToken.
- Binding cleanup tiep tuc co `_binding = null` trong cac fragments presentation.

### 11. Build status

Lenh build da chay:

```powershell
.\gradlew.bat :app:assembleDebug
```

Ket qua:

- Build thanh cong.
- Khong can set JAVA_HOME tam trong lan build Phase 3C.

### 12. Van de con lai

#### Critical

- Khong co.

#### High

- Khong co loi build/source blocker sau Phase 3C.

#### Medium

- Loading/error state van la foundation logic, chua co skeleton/progress/error layout rieng vi chua co API.
- Cac list van la static XML cards; RecyclerView/Adapter nen de phase API/dynamic data.
- Mot so disabled placeholder controls van cho phase backend/business logic sau.

#### Low

- ContentDescription cua card dang dung title/name chinh, co the mo rong thanh composite description khi item chuyen sang dynamic model.
- Thu muc hien tai khong phai git repository nen khong co git diff/status de luu metadata thay doi.

### 13. Ket luan

**READY_FOR_PHASE_4A**

- `assembleDebug` build thanh cong.
- Phase 3C da polish interaction/touch/a11y co ban cho cac surface chinh.
- Navigation/back audit khong phat hien mismatch action id.
- Khong goi API that.
- Khong tao repository/backend/database/storage logic that.
- Khong vi pham Android Native XML + Kotlin stack.
- San sang sang Phase 4A API integration theo architecture da dinh.

## Phase 0 - Frontend UI Audit Current Repo Re-Audit

### 1. Tong quan repo hien tai

- Da doc `docs/FE_Architecture.md`, `docs/SMARTRENT_MOBILE_UI_GUIDE.md`, Repo Taste Skill tai `.agents/skills/design-taste-frontend/SKILL.md`; root `SKILL.md` khong ton tai trong repo.
- Repo hien tai dang dung Android Native XML + Kotlin, single module `:app`, namespace/applicationId `com.example.rent`.
- Stack thuc te: `AppCompatActivity`, Fragment, ViewBinding, Navigation Component XML, Material Components, ConstraintLayout.
- Khong thay source Compose: khong co `@Composable`, khong co `setContent {}`, khong bat `buildFeatures.compose`; co transitive compose runtime annotation trong build output nhung khong phai Compose UI stack.
- UI guide chuan: `docs/SMARTRENT_MOBILE_UI_GUIDE.md`.
- Architecture chuan: `docs/FE_Architecture.md`; voi repo XML thi can dien giai Screen thanh Activity/Fragment/Adapter/XML layout nhu tai lieu cho phep.
- Repo da co design tokens XML kha day du: `colors.xml`, `dimens.xml`, `styles.xml`, `themes.xml`, drawable background/card/chip/input.
- Phase nay chi audit va cap nhat log, khong sua code production, khong tao component, khong thay doi layout/source/resource production.

### 2. Man hinh va navigation tim thay

- Activity: `MainActivity`.
- Auth: `AuthFragment`, `LoginFragment`, `RegisterFragment`, `ForgotPasswordFragment`.
- Main tabs: `HomeFragment`, `RoomsFragment`, `TenantsFragment`, `BillsFragment`, `ProfileFragment`.
- Rooms: `RoomsFragment`, `RoomDetailFragment`, `AddEditRoomFragment`.
- Tenants: `TenantsFragment`, `TenantDetailFragment`, `AddEditTenantFragment`.
- Bills/payment: `BillsFragment`, `BillDetailFragment`, `AddEditBillFragment`, `MeterReadingFragment`, `PaymentRecordFragment`.
- Notifications/settings: `NotificationsFragment`, `NotificationDetailFragment`, `SettingsFragment`.
- Navigation graph: `app/src/main/res/navigation/main_nav_graph.xml`, start destination `loginFragment`.
- Bottom navigation: `homeFragment`, `roomsFragment`, `tenantsFragment`, `billsFragment`, `profileFragment`.
- Bottom nav duoc an tren cac man ngoai main destinations bang destination listener trong `MainActivity`.

### 3. Component/shared foundation tim thay

- Shared state: `core/common/UiState.kt` voi Loading, Success, Empty, Error.
- Mock data provider: `core/mock/SmartRentMockData.kt`.
- UI helper: `core/ui/UiFeedback.kt`.
- Utils: `DisplayFormatters.kt`, `FormValidators.kt`.
- Route constants: `navigation/AppRoutes.kt`, hien moi la constants, navigation thuc te dung XML graph id.
- Shared XML styles:
  - `TextAppearance.SmartRent.TitleLarge`
  - `TextAppearance.SmartRent.TitleMedium`
  - `TextAppearance.SmartRent.SectionTitle`
  - `TextAppearance.SmartRent.Body`
  - `TextAppearance.SmartRent.BodySecondary`
  - `TextAppearance.SmartRent.Caption`
  - `Widget.SmartRent.Button.Primary`
  - `Widget.SmartRent.Button.Secondary`
  - `Widget.SmartRent.Button.Text`
  - `Widget.SmartRent.TextInputLayout`
  - `Widget.SmartRent.TextInputEditText`
  - `Widget.SmartRent.Card`
  - `Widget.SmartRent.Card.Elevated`
  - `Widget.SmartRent.Card.Compact`
  - `Widget.SmartRent.Toolbar`
- Shared drawables: `bg_app.xml`, `bg_card.xml`, `bg_chip.xml`, `bg_input.xml`, bottom nav icons, notification icon.

### 4. Do khop voi FE_Architecture.md

- Khop mot phan:
  - Co package `core`, `data`, `domain`, `navigation`, `presentation`.
  - Co single Activity + Fragment navigation foundation.
  - Co `UiState` foundation.
  - Co mock UI models rieng trong `presentation/model`.
- Chua khop:
  - Chua co ViewModel rieng cho moi man hinh.
  - Chua co `StateFlow`, Event, Effect, one-shot effect channel.
  - `data` va `domain` hien chu yeu la `.gitkeep`, chua co model/usecase/repository interface.
  - Chua co `feature/` theo feature-first module trong tai lieu.
  - Chua co Hilt, Retrofit, Room, DataStore, repository/usecase.
  - Presentation hien dang gom Fragment theo folder nghiep vu, chua tach adapter/component state day du.
  - Package name van la `com.example.rent`, lech voi identity goi y `com.smartrent.renthub`.

### 5. Theme, color, typography, shape, spacing

- Color tokens bam kha sat UI guide: brand `#2F6FED`, neutral background/surface/border/text, semantic success/warning/error/info/vacant va container colors.
- Co trung lap token `color_*` va `sr_*`; nen chon `sr_*` lam canonical khi refactor.
- Spacing tokens co day du scale 4/8/12/16/20/24/32dp va component heights 48/56/64dp.
- Radius tokens co `radius_sm`, `radius_md`, `radius_lg`, `radius_xl`, `radius_full`.
- Typography XML styles co scale title/body/caption theo UI guide, nhung fontFamily dang la `sans`, chua co Inter/self-hosted font.
- Theme light/night co Material3 DayNight parent, nhung night theme moi o muc an toan, chua phai dark mode duoc design/audit day du.

### 6. Hard-code audit

- Mau hard-code:
  - Khong thay mau hex rai trong layout/Kotlin production ngoai token file va launcher/vector asset.
  - `colors.xml` hien chua chuan hoa mot nguon duy nhat vi co ca `color_*` va `sr_*`.
- Spacing/text style:
  - Layout chinh da dung `@dimen` va `TextAppearance.SmartRent.*` la chu yeu.
  - Van co kich thuoc intrinsic hop ly trong vector icon 24dp va avatar profile 64dp.
- Text/business mock:
  - XML dung `@string` kha nhat quan.
  - `SmartRentMockData.kt` van hard-code mock display text, amount, date, status note bang string literal. Chap nhan cho UI mock, nhung can chuyen sang data layer/resource/dynamic model khi vao API.
- Component duplication:
  - Cac card list Room/Tenant/Bill/Notification dang lap lai XML item thay vi adapter/reusable row/card pattern.

### 7. Van de UI so voi SMARTRENT_MOBILE_UI_GUIDE.md

- Rooms guide goi y grid 2 cot/FAB; hien list la vertical static cards va top add button, chua co FAB.
- Bill/Room/Tenant lists chua co RecyclerView/Adapter, pull-to-refresh, swipe action, infinite loading, skeleton.
- Forms cho chon phong/ngay/phuong thuc hien con la input/disabled placeholder, trong guide nen dung bottom sheet/date picker.
- Empty state da co title/body/action nhung chua co icon/minh hoa 80-120px nhu guide.
- Loading/error state moi la foundation logic trong Fragment, chua co visual skeleton/progress/error retry layout.
- Notification card guide goi y icon/type structure; hien cards chu yeu text/card, chua co icon taxonomy ro.
- Room detail guide goi y banner/thumbnail room va tabs con; hien chi la card sections.
- Typography chua dung Inter; dang dung system sans.
- Night/dark mode chua duoc polish theo token contrast that.

### 8. Component nen refactor truoc

- List item/card pattern cho Rooms, Tenants, Bills, Notifications: nen dua ve RecyclerView Adapter hoac reusable include/component truoc khi gan API.
- Status chip pattern: can centralized mapping status -> label/color/contentDescription de tranh sai mau/sai semantics.
- Screen state layout: shared loading/empty/error include hoac component pattern cho XML.
- Form selection controls: room/date/payment method nen thanh bottom sheet/date picker pattern theo guide.
- Header/action pattern: chuan hoa header, add action, filter row, title/subtitle.
- Mock data/render split: chuyen static XML item sang render tu model de API phase khong phai rewrite layout hang loat.

### 9. Rui ro neu upgrade toan bo UI

- Rui ro regression navigation cao neu vua doi layout vua doi graph/action/back stack.
- Rui ro scope creep neu migrate Compose trong khi repo hien da chot XML + ViewBinding.
- Rui ro duplicate source of truth vi hien co ca XML static cards va `SmartRentMockData` models.
- Rui ro mat consistency mau/status neu moi man tu map status rieng.
- Rui ro build/resource churn neu thay token/theme qua rong truoc khi co dynamic data.
- Rui ro accessibility neu them custom card/bottom sheet/filter ma khong audit focus order, contentDescription, touch target.
- Rui ro API integration cham neu chua refactor list static sang adapter/model truoc.

### 10. Ke hoach upgrade theo phase

- Phase 1: chot XML la stack, giu Navigation Fragment, cleanup token source of truth, chuan hoa shared XML styles.
- Phase 2: refactor list static sang model-driven list pattern theo tung feature, uu tien Rooms, Tenants, Bills.
- Phase 3: chuan hoa status chip, empty/loading/error visual state, accessibility labels, touch targets, filter row behavior.
- Phase 4: refactor forms theo guide: bottom sheet/date picker/selection pattern, validation consistency.
- Phase 5: API integration: ViewModel + StateFlow + repository/usecase theo FE_Architecture, gan data vao UI da model-driven.
- Phase 6: dark mode, large font scale, small/large screen QA, TalkBack pass, final visual audit.

### 11. Lenh check/build co the chay

```powershell
.\gradlew.bat :app:assembleDebug
.\gradlew.bat :app:testDebugUnitTest
.\gradlew.bat :app:connectedDebugAndroidTest
rg -n '@Composable|setContent|buildFeatures.compose|compose' app build.gradle.kts gradle/libs.versions.toml
rg -n '#[0-9A-Fa-f]{6}|#[0-9A-Fa-f]{8}' app/src/main/res app/src/main/java/com/example/rent
rg -n 'android:text=\"[^@]|android:hint=\"[^@]|Snackbar\.make\([^,]+,\s*\"' app/src/main/res/layout app/src/main/java/com/example/rent
```

Ghi chu: trong lan audit nay khong chay build de giu phase dung tinh chat read-only/report-only. Lan build gan nhat trong log Phase 3C da thanh cong voi `.\gradlew.bat :app:assembleDebug`.

### 12. Ket luan Phase 0

**READY_WITH_MINOR_FIXES**

- Repo hien tai da co nen XML + Kotlin + ViewBinding + Navigation + Material Components kha on.
- UI tokens va style foundation da ton tai va bam kha sat mobile UI guide.
- Chua nen upgrade toan bo UI mot lan; nen refactor theo phase, uu tien list static -> model-driven, status chip, state layouts va form selection controls.
- Khong sua code production trong Phase 0 audit nay.

## Phase 1 - Theme / Design Tokens Standardization

### 1. Ngay thuc hien

- 2026-06-23.

### 2. Muc tieu phase

Phase 1 chi chuan hoa theme va design tokens cho SmartRent/RentHub mobile frontend. Phase nay khong upgrade tung man hinh, khong tao component moi, khong sua backend/API/repository/usecase/domain model/ViewModel/navigation/business logic va khong thay doi layout production.

### 3. Tai lieu va skill da doc

- Da doc `docs/FE_Architecture.md`.
- Da doc `docs/SMARTRENT_MOBILE_UI_GUIDE.md`.
- Root `SKILL.md`: khong ton tai trong repo, da ghi nhan.
- Da doc `.agents/skills/design-taste-frontend/SKILL.md`.
- Da doc `.agents/skills/design-taste-frontend-v1/SKILL.md`.
- Da doc `.agents/skills/full-output-enforcement/SKILL.md`.
- Da doc `.agents/skills/gpt-taste/SKILL.md`.
- Da doc `.agents/skills/high-end-visual-design/SKILL.md`.
- Da doc `.agents/skills/imagegen-frontend-mobile/SKILL.md`.
- Da doc `.agents/skills/redesign-existing-projects/SKILL.md`.
- Da doc `.agents/skills/stitch-design-taste/SKILL.md`.

Uu tien ap dung: `FE_Architecture.md`, `SMARTRENT_MOBILE_UI_GUIDE.md`, sau do moi den cac taste skills. Cac skill duoc dung nhu lens phu ve hierarchy, consistency, token discipline, readability va mobile polish; khong dua web/Tailwind/React/Compose vao project.

### 4. Audit summary

- Repo hien tai la Android Native XML + Kotlin, single module `:app`.
- UI stack thuc te: `AppCompatActivity`, Fragment, ViewBinding, Navigation Component XML, Material Components, ConstraintLayout.
- Khong co Compose UI source: khong co `@Composable`, khong co `setContent {}`, khong bat `buildFeatures.compose`. Co transitive Compose runtime annotation trong build output do dependency, khong phai UI stack.
- Theme/token files hien co: `colors.xml`, `dimens.xml`, `styles.xml`, `themes.xml`, `values-night/themes.xml`.
- Architecture hien tai van dung package `core`, `data`, `domain`, `navigation`, `presentation`; chua co ViewModel/StateFlow/repository/usecase day du nhu FE architecture.
- Layout hien tai chu yeu da dung `@dimen` va `TextAppearance.SmartRent.*`; hard-code hex ngoai token khong thay trong focused scan, tru launcher vector assets.

### 5. File da sua

- `app/src/main/res/values/colors.xml`.
- `app/src/main/res/values/dimens.xml`.
- `app/src/main/res/values/styles.xml`.
- `app/src/main/res/values/themes.xml`.
- `app/src/main/res/values-night/themes.xml`.
- `docs/frontend_phase_change_log.md`.

Khong tao file production moi va khong sua Fragment/layout/navigation/source logic.

### 6. Color tokens da chuan hoa

- Brand/app: `sr_primary`, `sr_primary_dark`, `sr_primary_light`, `sr_secondary`, `sr_background`, `sr_surface`, `sr_surface_elevated`, `sr_surface_pressed`, `sr_overlay_scrim`, `sr_focus_ring`.
- Text/border/disabled: `sr_text_primary`, `sr_text_secondary`, `sr_text_muted`, `sr_border`, `sr_divider`, `sr_disabled`, `sr_disabled_container`.
- General status: `sr_success`, `sr_success_text`, `sr_success_container`, `sr_warning`, `sr_warning_text`, `sr_warning_container`, `sr_error`, `sr_error_text`, `sr_error_container`, `sr_info`, `sr_info_text`, `sr_info_container`, `sr_neutral_text`, `sr_neutral_container`, `sr_vacant`, `sr_vacant_text`, `sr_vacant_container`.
- Room status: vacant, occupied, maintenance, inactive, moi status co `*_bg` va `*_text`.
- Invoice status: draft, processing, published, partially paid, paid, overdue, cancelled, moi status co `*_bg` va `*_text`.
- Payment status: pending, confirmed, rejected, moi status co `*_bg` va `*_text`.
- Ticket status: open, in progress, resolved, cancelled, moi status co `*_bg` va `*_text`.
- Legacy `color_*` aliases duoc giu lai de khong pha layout/resource hien co; `sr_*` la canonical cho phase sau.

### 7. Typography, spacing, radius va elevation tokens

- Typography scale: `text_display`, `text_h1`, `text_h2`, `text_h3`, `text_body`, `text_body_small`, `text_label`, `text_money`, `text_caption`.
- Text appearances moi: `TextAppearance.SmartRent.Display`, `H1`, `H2`, `H3`, `BodySmall`, `Label`, `Money`; cac style cu `TitleLarge`, `TitleMedium`, `SectionTitle`, `Body`, `BodySecondary`, `Caption` duoc giu.
- Spacing scale: `space_xs`, `space_sm`, `space_md`, `space_lg`, `space_xl`, `space_xxl`, them semantic spacing nhu `screen_padding_horizontal`, `screen_padding_vertical`, `screen_padding_bottom`, `section_spacing`, `component_gap`, `form_field_gap`, `card_padding`, `card_padding_compact`.
- Radius tokens: `radius_sm`, `radius_md`, `radius_lg`, `radius_xl`, `radius_full`, them semantic radius cho chip/input/button/card/bottom sheet/avatar.
- Elevation tokens: `elevation_card`, `elevation_nav`, `elevation_raised`, `elevation_sheet`.

### 8. Styles/theme da chuan hoa

- Button styles giu compatibility, them `Widget.SmartRent.Button.Danger`.
- Input/Card styles tro ve semantic radius token.
- Them `Widget.SmartRent.StatusChip` lam style foundation cho status chip phase sau, chua gan vao man hinh.
- Them shape appearances cho button/input/card/bottom sheet.
- Light theme them window background, light navigation bar, control activated/normal va colorOnError.
- Night theme dong bo cac theme attrs co ban, van giu muc an toan; chua polish dark mode day du.

### 9. Hard-code con lai nhung khong sua trong phase nay

- Focused scan khong thay hard-coded hex color, dp padding/margin hoac sp textSize trong layout/source ngoai token file va launcher vector assets.
- Launcher vector assets van co hex noi tai cua Android launcher icon, khong sua vi ngoai scope UI token phase.
- Mock display text, amount, date, status note trong `SmartRentMockData.kt` van la static demo data, khong sua vi khong phai theme/token.
- Static XML cards va duplicated status presentation trong cac man hinh van de Phase 2 refactor, khong sua trong Phase 1.
- Chua gan status color tokens moi vao UI vi gan vao screen/component la ngoai scope phase nay.

### 10. Lenh check/build

```powershell
.\gradlew.bat :app:assembleDebug
rg -n --glob '!build/**' '@Composable|setContent|buildFeatures.compose|androidx\.compose' .
rg -n --glob '!build/**' --glob '!app/src/main/res/values/colors.xml' --glob '!app/src/main/res/drawable/ic_launcher*' '#[0-9A-Fa-f]{6,8}' app/src/main/res app/src/main/java
rg -n --glob '!build/**' 'android:textSize="[0-9]+sp"|android:padding[A-Za-z]*="[0-9]+dp"|android:layout_margin[A-Za-z]*="[0-9]+dp"' app/src/main/res/layout app/src/main/res/drawable app/src/main/java
```

Build da chay:

```powershell
.\gradlew.bat :app:assembleDebug
```

Ket qua: `BUILD SUCCESSFUL` trong 8s.

### 11. Rui ro con lai

- FE architecture uu tien Compose, nhung repo hien tai da di theo XML/ViewBinding. Phase nay tiep tuc XML de tranh migrate lon ngoai scope.
- Token da day du hon nhung UI hien tai chua su dung tat ca semantic token moi; phase sau can map status tap trung de tranh moi man hinh tu xu ly mau rieng.
- Night theme chua phai dark mode hoan chinh; can mot pass rieng ve contrast va colors-night neu yeu cau dark mode that.
- Neu upgrade toan bo UI mot lan, rui ro cao o navigation, static XML cards, mock data va status mapping.

### 12. De xuat Phase 2 shared components

- Tao pattern status chip centralized: status -> label, bg token, text token, contentDescription.
- Refactor list/card lap lai thanh model-driven row/card pattern cho Rooms, Tenants, Bills, Notifications.
- Tao shared loading/empty/error state layout hoac include pattern.
- Chuan hoa header/action/filter row va add action/FAB theo guide.
- Chuan hoa form selection controls: room picker, date picker, payment method bottom sheet.
- Sau khi shared components on dinh moi gan ViewModel/StateFlow/repository/usecase theo `FE_Architecture.md`.

### 13. Ket luan Phase 1

**READY_FOR_PHASE_2_SHARED_COMPONENTS**

- Token/theme da duoc centralize them mot buoc va compile thanh cong.
- Khong sua screen UI, component production, navigation, business logic, API, domain, repository hay ViewModel.
- Build `:app:assembleDebug` thanh cong.

## Phase 2 - Shared Components

### 1. Ngay thuc hien

- 2026-06-23.

### 2. Trang thai

**BLOCKED_BY_REQUIRED_SKILL_FILE**

Phase 2 chua duoc thuc hien. Theo brief Phase 2, truoc khi sua shared components bat buoc phai doc `SKILL.md` root repo va toan bo cac skill duoc liet ke. Neu file/skill bat buoc khong ton tai hoac khong doc duoc thi phai ghi ro trong log va dung lai de user kiem tra.

### 3. Kiem tra dieu kien bat dau Phase 2

- `docs/frontend_phase_change_log.md` da co log Phase 1: co.
- Theme/design tokens tu Phase 1 da ton tai:
  - `app/src/main/res/values/colors.xml`: co status semantic tokens nhu `sr_invoice_overdue_bg`.
  - `app/src/main/res/values/dimens.xml`: co semantic spacing token nhu `screen_padding_horizontal`.
  - `app/src/main/res/values/styles.xml`: co shared style foundation nhu `Widget.SmartRent.StatusChip`.
- `docs/FE_Architecture.md`: ton tai.
- `docs/SMARTRENT_MOBILE_UI_GUIDE.md`: ton tai.
- Root `SKILL.md`: khong ton tai, khong the doc.

### 4. Skill/file availability

- `SKILL.md` root repo: **MISSING**.
- `.agents/skills/design-taste-frontend/SKILL.md`: ton tai.
- `.agents/skills/design-taste-frontend-v1/SKILL.md`: ton tai.
- `.agents/skills/full-output-enforcement/SKILL.md`: ton tai.
- `.agents/skills/gpt-taste/SKILL.md`: ton tai.
- `.agents/skills/high-end-visual-design/SKILL.md`: ton tai.
- `.agents/skills/imagegen-frontend-mobile/SKILL.md`: ton tai.
- `.agents/skills/redesign-existing-projects/SKILL.md`: ton tai.
- `.agents/skills/stitch-design-taste/SKILL.md`: ton tai.

### 5. Pham vi da thuc hien trong lan nay

- Chi kiem tra prerequisite Phase 2.
- Chi cap nhat changelog nay.
- Khong audit shared components sau diem blocked.
- Khong sua `presentation/component/`, `ui/component/`, `common/ui/`, `shared/component/`.
- Khong sua Fragment/layout/navigation/source logic.
- Khong sua backend/API/repository/usecase/domain model/ViewModel/business logic.

### 6. Lenh check da chay

```powershell
Test-Path 'docs/FE_Architecture.md'
Test-Path 'docs/SMARTRENT_MOBILE_UI_GUIDE.md'
Test-Path 'SKILL.md'
Test-Path 'docs/frontend_phase_change_log.md'
Select-String -Path 'docs/frontend_phase_change_log.md' -Pattern 'Phase 1 - Theme / Design Tokens Standardization'
Select-String -Path 'app/src/main/res/values/colors.xml','app/src/main/res/values/dimens.xml','app/src/main/res/values/styles.xml' -Pattern 'sr_invoice_overdue_bg|screen_padding_horizontal|Widget.SmartRent.StatusChip'
```

Ket qua: Phase 1 va tokens da san sang, nhung root `SKILL.md` missing.

### 7. Build/check status

- Khong chay `.\gradlew.bat :app:assembleDebug` vi phase dung truoc khi co thay doi production/shared component.
- Khong co compile error moi duoc tao trong lan nay.

### 8. Viec can lam de tiep tuc

- Bo sung hoac khoi phuc root `SKILL.md` tai repo root `C:\Users\thnh2k5\AndroidStudioProjects\Rent\SKILL.md`, hoac xac nhan ro rang rang Phase 2 duoc phep bo qua root `SKILL.md`.
- Sau khi dieu kien nay duoc giai quyet, Phase 2 co the tiep tuc: doc day du docs/skills, audit shared components, nang cap shared components theo token Phase 1, build va cap nhat changelog.

## Phase 2 - Shared Components Resumed

### 1. Ngay thuc hien

- 2026-06-23.

### 2. Trang thai

**READY_FOR_PHASE_3_AUTH_APP_SHELL_NAVIGATION_UI**

Phase 2 da duoc tiep tuc theo brief moi khong yeu cau root `SKILL.md`. Lan nay chi chuan hoa shared component resources cho Android XML, khong upgrade sau tung man hinh feature, khong sua navigation flow va khong sua business/API/domain/ViewModel logic.

### 3. Tai lieu va skill da doc

- Da doc `docs/FE_Architecture.md`.
- Da doc `docs/SMARTRENT_MOBILE_UI_GUIDE.md`.
- Da doc `.agents/skills/design-taste-frontend/SKILL.md`.
- Da doc `.agents/skills/design-taste-frontend-v1/SKILL.md`.
- Da doc `.agents/skills/full-output-enforcement/SKILL.md`.
- Da doc `.agents/skills/gpt-taste/SKILL.md`.
- Da doc `.agents/skills/high-end-visual-design/SKILL.md`.
- Da doc `.agents/skills/imagegen-frontend-mobile/SKILL.md`.
- Da doc `.agents/skills/redesign-existing-projects/SKILL.md`.
- Da doc `.agents/skills/stitch-design-taste/SKILL.md`.
- Skill khong doc duoc: khong co.

Nguyen tac tong hop ap dung: giu dung stack XML/ViewBinding hien tai, uu tien design guide va token Phase 1, khong tao style roi rac, moi shared component phai co state ro rang, shadow nhe, radius/spacing/mobile touch target nhat quan va khong dua logic nghiep vu vao component dung chung.

### 4. Audit shared components hien tai

- Repo khong co package `presentation/component/`, `ui/component/`, `common/ui/` hoac `shared/component/` rieng cho view component.
- Shared component layer hien tai nam chu yeu o XML resources:
  - `TextAppearance.SmartRent.*`.
  - `Widget.SmartRent.Button.*`.
  - `Widget.SmartRent.TextInputLayout`.
  - `Widget.SmartRent.TextInputEditText`.
  - `Widget.SmartRent.Card.*`.
  - `Widget.SmartRent.StatusChip`.
  - `Widget.SmartRent.Toolbar`.
  - Drawables `bg_app`, `bg_card`, `bg_chip`, `bg_input`.
  - Color selector `bottom_nav_item_color`.
- Shared Kotlin UI helper hien co: `core/ui/UiFeedback.kt`, gom Snackbar, hide keyboard va clear TextInputLayout error on text change.
- Component duoc dung nhieu nhat: Button, TextInputLayout/EditText, Card, TextAppearance va generic `bg_chip` trong layout XML.
- Duplicate/chua day du: chua co style rieng cho loading button, icon button, FAB, filter chip, state card, loading indicator, bottom sheet va bottom navigation; generic chip chua co disabled/pressed state ro.

### 5. Component da nang cap

- Button:
  - Chuan hoa `Widget.SmartRent.Button.Primary`, `Secondary`, `Text`, `Danger` dung color state list cho disabled/pressed/content.
  - Them `Widget.SmartRent.Button.Loading` de giu min width on dinh cho loading state.
  - Them `Widget.SmartRent.IconButton` va `Widget.SmartRent.FloatingActionButton`.
- Input/TextField:
  - `Widget.SmartRent.TextInputLayout` dung selector `sr_text_input_box_stroke` cho enabled/disabled/focused; error van dung `boxStrokeErrorColor`.
  - Them `Widget.SmartRent.TextInputLayout.Search` va `Widget.SmartRent.TextInputEditText.Money`.
- Card:
  - `Widget.SmartRent.Card` dung ripple token, radius token va border/elevation token.
  - Them `Widget.SmartRent.Card.Clickable`, `Widget.SmartRent.Card.State`, `Widget.SmartRent.Card.Loading`.
- Badge/Status/Filter:
  - `Widget.SmartRent.StatusChip` dung selector bg/text cho fallback neutral.
  - Them variant `Success`, `Warning`, `Error`, `Info`, `Vacant`.
  - Them `Widget.SmartRent.FilterChip` voi selected/unselected/disabled state.
- Empty/Error/Loading:
  - Them state icon backgrounds `bg_empty_state_icon`, `bg_error_state_icon`.
  - Them `bg_loading_skeleton`.
  - Them `Widget.SmartRent.LoadingIndicator`.
- TopBar/Header:
  - Giu `Widget.SmartRent.Toolbar`, tiep tuc dung token title/subtitle/navigation icon.
- BottomNavigation/App Shell:
  - Them `Widget.SmartRent.BottomNavigation` va gan qua theme `bottomNavigationStyle`.
  - Cap nhat `bottom_nav_item_color` co disabled state.
- Dialog/BottomSheet:
  - Them `Widget.SmartRent.BottomSheet.Modal`, `bg_bottom_sheet`, `bg_bottom_sheet_handle`.
  - Gan qua theme `bottomSheetStyle`.

### 6. File da tao/sua

- Tao moi color selectors:
  - `app/src/main/res/color/sr_button_primary_background.xml`
  - `app/src/main/res/color/sr_button_on_primary.xml`
  - `app/src/main/res/color/sr_button_secondary_background.xml`
  - `app/src/main/res/color/sr_button_secondary_content.xml`
  - `app/src/main/res/color/sr_button_danger_background.xml`
  - `app/src/main/res/color/sr_text_button_content.xml`
  - `app/src/main/res/color/sr_text_input_box_stroke.xml`
  - `app/src/main/res/color/sr_status_chip_background.xml`
  - `app/src/main/res/color/sr_status_chip_text.xml`
  - `app/src/main/res/color/sr_filter_chip_background.xml`
  - `app/src/main/res/color/sr_filter_chip_content.xml`
- Tao moi drawables:
  - `app/src/main/res/drawable/bg_empty_state_icon.xml`
  - `app/src/main/res/drawable/bg_error_state_icon.xml`
  - `app/src/main/res/drawable/bg_loading_skeleton.xml`
  - `app/src/main/res/drawable/bg_bottom_sheet.xml`
  - `app/src/main/res/drawable/bg_bottom_sheet_handle.xml`
- Sua:
  - `app/src/main/res/color/bottom_nav_item_color.xml`
  - `app/src/main/res/drawable/bg_card.xml`
  - `app/src/main/res/drawable/bg_chip.xml`
  - `app/src/main/res/drawable/bg_input.xml`
  - `app/src/main/res/values/dimens.xml`
  - `app/src/main/res/values/styles.xml`
  - `app/src/main/res/values/themes.xml`
  - `app/src/main/res/values-night/themes.xml`
  - `docs/frontend_phase_change_log.md`

### 7. Component giu nguyen va ly do

- `core/ui/UiFeedback.kt`: giu nguyen vi dang la helper feedback/keyboard/error cleanup, khong co hard-code style va khong can doi API.
- Layout feature XML: giu nguyen de khong upgrade sau tung man hinh trong Phase 2.
- Navigation graph, `MainActivity`, Fragment Kotlin: giu nguyen de khong doi route/backstack/call-site.
- Status chip call-sites dang dung `bg_chip`: chua doi sang variant moi de tranh lan sang feature screens; phase sau co the map status theo model.

### 8. Token Phase 1 da ap dung

- Button: `sr_primary`, `sr_primary_dark`, `sr_primary_light`, `sr_error`, `sr_error_text`, `sr_error_container`, `sr_disabled_container`, `sr_text_muted`, `radius_button`, `button_height`.
- Input: `sr_surface`, `sr_border`, `sr_primary`, `sr_error`, `sr_disabled`, `radius_input`, `input_height`, `text_md`.
- Card: `sr_surface`, `sr_border`, `elevation_card`, `elevation_raised`, `radius_card`, `card_padding`, `card_padding_compact`.
- Badge/filter: `sr_neutral_container`, `sr_neutral_text`, `sr_success_container`, `sr_success_text`, `sr_warning_container`, `sr_warning_text`, `sr_error_container`, `sr_error_text`, `sr_info_container`, `sr_info_text`, `sr_vacant_container`, `sr_vacant_text`, `radius_chip`.
- Loading/empty/error: `sr_disabled_container`, `sr_neutral_container`, `sr_error_container`, `state_icon_size`, `loading_indicator_size`.
- Bottom navigation/FAB/bottom sheet: `bottom_nav_height`, `elevation_nav`, `elevation_sheet`, `fab_size`, `radius_full`, `radius_bottom_sheet`.

### 9. Breaking change va call-site

- Breaking change: khong co.
- Public XML style ten cu van duoc giu.
- Call-site da sua de compile: khong co layout/source call-site nao phai sua.
- Loi compile phat sinh trong phase: selector `android:state_error` khong hop le trong `sr_text_input_box_stroke.xml`; da sua bang cach bo state nay va dung `boxStrokeErrorColor` cua TextInputLayout cho error state.

### 10. Hard-code UI con lai

- Focused scan khong phat hien hard-coded hex/dp/sp trong shared component resources ngoai token files va launcher vector assets.
- Launcher icons van co mau noi tai, khong sua vi khong thuoc shared component UI.
- Feature layouts van con nhieu card/chip static va status presentation lap lai; de Phase 3+ refactor khi duoc phep cham vao Auth/App Shell/Navigation UI hoac tung feature.
- Static mock text/data trong `SmartRentMockData.kt` khong sua vi ngoai scope shared components.

### 11. Lenh build/check da chay

```powershell
.\gradlew.bat :app:assembleDebug
rg -n --glob '!build/**' --glob '!app/src/main/res/values/colors.xml' --glob '!app/src/main/res/drawable/ic_launcher*' '#[0-9A-Fa-f]{6,8}|android:textSize="[0-9]+sp"|android:padding[A-Za-z]*="[0-9]+dp"|android:layout_margin[A-Za-z]*="[0-9]+dp"' app/src/main/res app/src/main/java
```

Ket qua build:

- Lan 1 fail tai `:app:processDebugResources` do `android:state_error` khong ton tai trong color selector.
- Da sua loi resource do phase nay gay ra.
- Lan 2: `BUILD SUCCESSFUL` trong 3s.

### 12. Loi con lai

- Khong co loi build.
- Chua co class/component Kotlin rieng cho status mapping; hien tai moi co resource/style foundation. Nen tao khi bat dau gan vao feature/UI model de tranh hard-code status theo screen.

### 13. De xuat Phase 3: Auth + App Shell + Navigation UI

- Ap dung style shared moi vao Auth/App Shell/BottomNav neu scope Phase 3 cho phep sua layout.
- Chuan hoa App Shell: bottom nav style, screen padding bottom, header/title/action pattern.
- Chuan hoa Auth UI: loading button, input states, password toggle, error text consistency.
- Tao status mapper dung chung neu Phase 3 bat dau cham UI model/component call-site.
- Duy tri navigation flow hien tai, chi polish UI shell va call-site toi thieu.

## Phase 3 - Auth + App Shell + Navigation UI

### 1. Ngay thuc hien

- 2026-06-23.

### 2. Tai lieu va skill da doc

- Da doc `docs/FE_Architecture.md`.
- Da doc `docs/SMARTRENT_MOBILE_UI_GUIDE.md`.
- Da doc `.agents/skills/design-taste-frontend/SKILL.md`.
- Da doc `.agents/skills/design-taste-frontend-v1/SKILL.md`.
- Da doc `.agents/skills/full-output-enforcement/SKILL.md`.
- Da doc `.agents/skills/gpt-taste/SKILL.md`.
- Da doc `.agents/skills/high-end-visual-design/SKILL.md`.
- Da doc `.agents/skills/imagegen-frontend-mobile/SKILL.md`.
- Da doc `.agents/skills/redesign-existing-projects/SKILL.md`.
- Da doc `.agents/skills/stitch-design-taste/SKILL.md`.
- Skill khong doc duoc: khong co.

Nguyen tac ap dung: giu XML/ViewBinding hien tai, dung token Phase 1 va shared styles Phase 2, khong doi navigation flow, khong doi auth/session/business logic, va khong upgrade cac man nghiep vu ngoai scope.

### 3. Kiem tra dieu kien bat dau

- Phase 1 log: co.
- Phase 2 resumed log: co.
- Theme/design tokens Phase 1: co.
- Shared component resources Phase 2: co, gom `Widget.SmartRent.Button.Loading`, `Widget.SmartRent.BottomNavigation`, `Widget.SmartRent.Card.State` va cac selector/drawable shared.

### 4. Audit Auth/App Shell/Navigation hien tai

- Auth screens hien co: `LoginFragment`, `RegisterFragment`, `ForgotPasswordFragment`, `AuthFragment` legacy placeholder.
- Khong co Splash screen, First Login Change Password screen hoac Space Selection screen trong source hien tai.
- App shell hien tai: `activity_main.xml` chua `NavHostFragment` va `BottomNavigationView`; `MainActivity` setup `NavController`, bottom nav va an/hien theo destination.
- Navigation graph: `main_nav_graph.xml`, start destination van la `loginFragment`.
- Bottom navigation: 5 tab hien co `homeFragment`, `roomsFragment`, `tenantsFragment`, `billsFragment`, `profileFragment`.
- TopBar/Header: chua co AppTopBar rieng; tung man dang dung header/title trong layout XML. Phase nay chi polish header Auth trong scope.
- Lech UI guide truoc Phase 3: Auth form con rai rac, brand header chua that ro, button chua dung loading/shared style moi, bottom nav con khai bao inline thay vi dung shared style Phase 2.

### 5. Man hinh Auth da nang cap

- `fragment_login.xml`:
  - Chuyen sang brand header + form card mobile-first.
  - Giu id `contactInputLayout`, `contactEditText`, `passwordInputLayout`, `passwordEditText`, `forgotPasswordButton`, `loginButton`, `openRegisterButton`.
  - Dung `Widget.SmartRent.Card`, `Widget.SmartRent.TextInputLayout`, `Widget.SmartRent.TextInputEditText`, `Widget.SmartRent.Button.Loading`, `Widget.SmartRent.Button.Text`.
  - Giu password toggle san co va validation/click logic hien tai.
- `fragment_register.xml`:
  - Chuyen sang form card gon, label/input ro, CTA chinh noi bat.
  - Giu toan bo binding id de khong doi Fragment logic.
  - Dung shared input/button/card style tu Phase 2.
- `fragment_forgot_password.xml`:
  - Chuyen sang form card + info state card.
  - Thay note hien thi mang tinh "phase/backend" bang note nguoi dung ro rang hon.
  - Giu logic gui yeu cau mock va back to login.

### 6. App Shell/Navigation UI da sua

- `activity_main.xml`:
  - Root background dung `@color/sr_background`.
  - `BottomNavigationView` dung `@style/Widget.SmartRent.BottomNavigation`.
  - Giu `@menu/bottom_nav_menu`, constraint va id cu.
- `MainActivity.kt`: giu nguyen.
- `main_nav_graph.xml`: giu nguyen.
- `bottom_nav_menu.xml`: giu nguyen, khong them tab/route moi.

### 7. Token/shared components da ap dung

- `screen_padding_horizontal`, `space_24`, `section_spacing`, `form_field_gap`, `card_padding`.
- `TextAppearance.SmartRent.H1`, `H2`, `BodySmall`, `BodySecondary`, `Caption`.
- `Widget.SmartRent.Card`, `Card.State`, `TextInputLayout`, `TextInputEditText`, `Button.Loading`, `Button.Text`, `BottomNavigation`.
- `sr_background`, `sr_surface`, `sr_primary`, `sr_primary_light`, `sr_info_container`, `sr_info_text`.

### 8. Phan giu nguyen va ly do

- Splash, First Login Change Password, Space Selection: khong ton tai trong repo nen khong tao route/man moi.
- Auth Fragment Kotlin: giu nguyen de khong doi validation/navigation/session logic.
- Navigation graph va route ids: giu nguyen de khong doi flow.
- Rooms/Tenants/Bills/Profile/Notifications va cac man nghiep vu: khong sua vi ngoai scope Phase 3.
- `AuthFragment` legacy placeholder: giu nguyen de khong xoa man/module hien co.

### 9. Breaking change va call-site

- Breaking change: khong co.
- Call-site phai sua de compile: khong co Kotlin/source call-site nao phai sua.
- Binding ids duoc giu nen Fragment compile khong can thay doi.

### 10. Hard-code UI con lai

- Focused scan trong Auth/App Shell scope khong phat hien hard-coded hex color, dp spacing hoac sp text size.
- Feature screens ngoai Auth van co nhieu static card/status layout tu cac phase truoc; de lai vi ngoai scope Phase 3.
- Mock auth messages trong Fragment van giu theo logic hien co; khong doi business/auth behavior trong phase nay.

### 11. Lenh build/check da chay

```powershell
.\gradlew.bat :app:assembleDebug
rg -n '#[0-9A-Fa-f]{6,8}|android:textSize="[0-9]+sp"|android:padding[A-Za-z]*="[0-9]+dp"|android:layout_margin[A-Za-z]*="[0-9]+dp"' app/src/main/res/layout/fragment_login.xml app/src/main/res/layout/fragment_register.xml app/src/main/res/layout/fragment_forgot_password.xml app/src/main/res/layout/activity_main.xml app/src/main/java/com/example/rent/MainActivity.kt app/src/main/java/com/example/rent/presentation/auth
```

Ket qua:

- `BUILD SUCCESSFUL` trong 3s.
- Hard-code scan trong scope Phase 3 khong tra ve ket qua.

### 12. Loi con lai

- Khong co loi build.
- Chua co AppTopBar shared component that su duoc gan vao cac man, vi phase nay khong sua feature screens ngoai Auth.
- Chua co Splash/Space Selection/First Login screen trong repo de upgrade.

### 13. De xuat Phase 4: Owner Home + Tenant Home

- Tach Home theo role owner/tenant neu architecture phase sau yeu cau.
- Ap dung shared status chip/filter/card/loading styles vao Home trong scope ro rang.
- Chuan hoa summary cards, pending action list va recent activity bang data/model-driven pattern.
- Khong gan API cho den khi ViewModel/StateFlow/repository/usecase phase duoc phep.

## Phase 4 - Owner Home + Tenant Home

### 1. Ngay thuc hien

- 2026-06-23.

### 2. Tai lieu va skill da doc

- Da doc `docs/FE_Architecture.md`.
- Da doc `docs/SMARTRENT_MOBILE_UI_GUIDE.md`.
- Da doc `.agents/skills/design-taste-frontend/SKILL.md`.
- Da doc `.agents/skills/design-taste-frontend-v1/SKILL.md`.
- Da doc `.agents/skills/full-output-enforcement/SKILL.md`.
- Da doc `.agents/skills/gpt-taste/SKILL.md`.
- Da doc `.agents/skills/high-end-visual-design/SKILL.md`.
- Da doc `.agents/skills/imagegen-frontend-mobile/SKILL.md`.
- Da doc `.agents/skills/redesign-existing-projects/SKILL.md`.
- Da doc `.agents/skills/stitch-design-taste/SKILL.md`.
- Skill khong doc duoc: khong co.

Nguyen tac ap dung: uu tien XML/ViewBinding hien tai, dung token Phase 1 va shared components Phase 2, giu App Shell/Navigation UI Phase 3, khong tao route moi, khong sua logic dashboard/auth/session/navigation va khong cham cac module Room/Tenant/Billing/Invoice/Payment/Ticket/Profile/Settings.

### 3. Kiem tra dieu kien bat dau

- Phase 1 log: co.
- Phase 2 resumed log: co.
- Phase 3 log: co.
- Theme/design tokens Phase 1: co.
- Shared components Phase 2: co va build duoc.
- App Shell/Navigation UI Phase 3: co va `activity_main.xml` dang dung `Widget.SmartRent.BottomNavigation`.

### 4. Audit Owner/Tenant Home hien tai

- Repo hien chi co `HomeFragment` va `fragment_home.xml` voi tab `homeFragment`.
- Khong co `OwnerHomeScreen`, `OwnerHomeViewModel`, `OwnerHomeUiState`.
- Khong co `TenantHomeScreen`, `TenantHomeViewModel`, `TenantHomeUiState`.
- Khong co route `owner_home` hoac `tenant_home`; `main_nav_graph.xml` chi co `homeFragment`.
- Du lieu dashboard hien tai den tu `SmartRentMockData.homeOverview`, `homeQuickActions`, `recentActivities` va cac string resource Home co san.
- Quick action hien co:
  - Them phong -> route rooms hien co.
  - Them nguoi thue -> route tenants hien co.
  - Tao hoa don -> route bills hien co.
  - Ghi dien/nuoc -> dang disabled vi chua co route dedicated tu Home.
- Diem lech UI guide truoc phase:
  - Revenue/month summary chua noi bat.
  - Warning/overdue card chua duoc uu tien dung semantic warning.
  - Dashboard metric cards con phang va lap lai, chua dung day du style Phase 2.
  - Tenant Home khong ton tai nen khong the upgrade ma khong them route moi.

### 5. Man hinh da nang cap

- `app/src/main/res/layout/fragment_home.xml`:
  - Chuan hoa thanh Owner Dashboard mobile-first.
  - Them header ro hon voi avatar initials, greeting, space name va icon notification.
  - Dua revenue monthly summary len card noi bat tren cung, dung `TextAppearance.SmartRent.Display`.
  - Chuan hoa overview metric cards cho tong phong, dang thue, phong trong va hoa don cho thu.
  - Dua warning/overdue invoice vao `Widget.SmartRent.Card.State` voi `Widget.SmartRent.StatusChip.Warning`.
  - Chuan hoa quick actions bang button styles Phase 2.
  - Chuan hoa recent activity thanh card gon, de quet mat.
  - Giu cac id binding: `notificationButton`, `addRoomButton`, `addTenantButton`, `createBillButton`, `recordMeterButton`, `openBillsReminderButton`.

### 6. Dashboard component da sua/tao

- Khong tao component Kotlin/XML include moi.
- Su dung lai shared component resources co san:
  - `Widget.SmartRent.Card`
  - `Widget.SmartRent.Card.Elevated`
  - `Widget.SmartRent.Card.State`
  - `Widget.SmartRent.StatusChip.Warning`
  - `Widget.SmartRent.Button.Primary`
  - `Widget.SmartRent.Button.Secondary`
  - `Widget.SmartRent.IconButton`
  - `TextAppearance.SmartRent.Display`, `H1`, `H2`, `H3`, `BodySmall`, `Caption`

### 7. Token/shared components da ap dung

- Spacing/padding: `screen_padding_horizontal`, `screen_padding_vertical`, `screen_padding_bottom`, `section_spacing`, `card_padding`, `card_padding_compact`, `space_1`, `space_2`, `space_4`, `space_8`, `space_12`.
- Color/status: `sr_primary`, `sr_primary_light`, `sr_info_container`, `sr_info_text`, `sr_vacant_container`, `sr_vacant_text`, `sr_warning_container`, `sr_warning_text`, `sr_divider`.
- Component sizes: `avatar_size_md`, `icon_button_size`, `stroke_thin`.
- Typography: Display for revenue, H1/H2/H3 for hierarchy, BodySmall/Caption for secondary metadata.

### 8. Phan giu nguyen va ly do

- `HomeFragment.kt`: giu nguyen de khong doi navigation/click/business state logic.
- `SmartRentMockData.kt` va `SmartRentUiModels.kt`: giu nguyen, khong them fake data dashboard moi.
- `main_nav_graph.xml`, `MainActivity.kt`, `bottom_nav_menu.xml`: giu nguyen de khong doi navigation flow.
- Tenant Home: khong ton tai trong repo; khong tu tao route/man moi vi brief cam doi route/navigation flow.
- Rooms/Tenants/Bills/Payment/Ticket/Profile/Settings screens: khong sua vi ngoai scope.

### 9. Breaking change va call-site

- Breaking change: khong co.
- Call-site phai sua de compile: khong co Kotlin/source call-site nao phai sua.
- Binding ids trong `fragment_home.xml` duoc giu nen `HomeFragment.kt` build khong can thay doi.

### 10. Hard-code UI con lai

- Focused scan trong Home scope khong phat hien hard-coded hex color, dp spacing hoac sp text size moi.
- Static strings/mock data Home van ton tai tu cac phase truoc, khong sua vi Phase 4 khong duoc thay data/business logic.
- Cac man ngoai Home van co static XML cards va se can phase rieng.

### 11. Lenh build/check da chay

```powershell
.\gradlew.bat :app:assembleDebug
rg -n '#[0-9A-Fa-f]{6,8}|android:textSize="[0-9]+sp"|android:padding[A-Za-z]*="[0-9]+dp"|android:layout_margin[A-Za-z]*="[0-9]+dp"' app/src/main/res/layout/fragment_home.xml app/src/main/java/com/example/rent/presentation/home app/src/main/java/com/example/rent/core/mock app/src/main/java/com/example/rent/presentation/model
```

Ket qua:

- `BUILD SUCCESSFUL` trong 2s.
- Hard-code scan trong Home scope khong tra ve ket qua.

### 12. Loi con lai

- Khong co loi build.
- Tenant Home chua co route/screen rieng.
- Home van dung static XML + mock data; chua co ViewModel/StateFlow theo architecture day du.

### 13. De xuat Phase 5: Room + Tenant Module

- Refactor Room/Tenant list cards theo shared card/status chip style.
- Chuyen static room/tenant cards sang model-driven row/card pattern truoc khi gan API.
- Tao status mapper dung chung cho RoomStatus/TenantStatus -> label/bg/text token.
- Giu navigation graph hien tai, chi sua Room/Tenant UI khi Phase 5 cho phep.

## Phase 5 - Room + Tenant Module UI

### 1. Ngay thuc hien

- 2026-06-23.

### 2. Tai lieu va skill da doc

- Da doc `docs/FE_Architecture.md`.
- Da doc `docs/SMARTRENT_MOBILE_UI_GUIDE.md`.
- Da doc `.agents/skills/design-taste-frontend/SKILL.md`.
- Da doc `.agents/skills/design-taste-frontend-v1/SKILL.md`.
- Da doc `.agents/skills/full-output-enforcement/SKILL.md`.
- Da doc `.agents/skills/gpt-taste/SKILL.md`.
- Da doc `.agents/skills/high-end-visual-design/SKILL.md`.
- Da doc `.agents/skills/imagegen-frontend-mobile/SKILL.md`.
- Da doc `.agents/skills/redesign-existing-projects/SKILL.md`.
- Da doc `.agents/skills/stitch-design-taste/SKILL.md`.
- Skill khong doc duoc: khong co.

Nguyen tac ap dung: giu XML/ViewBinding hien tai, khong tao route moi, khong sua ViewModel/UiState/Event/Effect, khong sua backend/API/repository/usecase/domain/business logic, khong them fake data, uu tien token Phase 1 va shared component styles Phase 2.

### 3. Kiem tra dieu kien bat dau

- Phase 1 log: co.
- Phase 2 resumed log: co.
- Phase 3 log: co.
- Phase 4 log: co.
- Theme/design tokens Phase 1: co.
- Shared components Phase 2: co, gom `Widget.SmartRent.Card.Clickable`, `Widget.SmartRent.StatusChip.*`, `Widget.SmartRent.FilterChip`, `Widget.SmartRent.TextInputLayout.Search`, `Widget.SmartRent.Button.Loading`.
- App Shell/Navigation UI Phase 3: co.
- Home Phase 4: co.

### 4. Audit Room/Tenant hien tai

- Repo dang dung Android XML + Fragment/ViewBinding cho Room/Tenant; khong co Jetpack Compose trong scope nay.
- Room screens:
  - `RoomsFragment.kt` + `fragment_rooms.xml`
  - `RoomDetailFragment.kt` + `fragment_room_detail.xml`
  - `AddEditRoomFragment.kt` + `fragment_add_edit_room.xml`
- Tenant screens:
  - `TenantsFragment.kt` + `fragment_tenants.xml`
  - `TenantDetailFragment.kt` + `fragment_tenant_detail.xml`
  - `AddEditTenantFragment.kt` + `fragment_add_edit_tenant.xml`
- Room/Tenant hien van la static XML cards voi mock data co san trong fragment/model hien huu; phase nay khong doi data source.
- UI lech guide truoc phase:
  - Status badges dung `TextView` + `bg_chip` thu cong.
  - Filter dang dung primary/secondary buttons thay vi filter chip.
  - List item cards chua dung `Card.Clickable`.
  - Mot so padding/section spacing con dung legacy `space_16/20/24/32`.
  - Gia tien chua dung money typography nhat quan.
  - Search Tenant chua dung search text input style Phase 2.

### 5. Man hinh da nang cap

- `app/src/main/res/layout/fragment_rooms.xml`:
  - Chuyen filter button sang `com.google.android.material.chip.Chip` voi `Widget.SmartRent.FilterChip`.
  - Chuyen room status sang `Widget.SmartRent.StatusChip.Info/Vacant/Warning`.
  - Chuyen room list cards sang `Widget.SmartRent.Card.Clickable`.
  - Ap dung `TextAppearance.SmartRent.Money` cho gia phong.
  - Ap dung `screen_padding_*`, `section_spacing`, `card_padding`, `card_padding_compact`.
- `app/src/main/res/layout/fragment_room_detail.xml`:
  - Chuyen status phong sang `StatusChip.Info`.
  - Dung `Card.Elevated` cho info card va `Card.State` cho billing warning card.
  - Dung money typography cho gia phong.
- `app/src/main/res/layout/fragment_add_edit_room.xml`:
  - Dung semantic screen padding, form gap, elevated form card va `Button.Loading` cho save button.
  - Dung `TextInputEditText.Money` cho truong gia phong.
- `app/src/main/res/layout/fragment_tenants.xml`:
  - Dung `TextInputLayout.Search` cho search.
  - Chuyen filter button sang `FilterChip`.
  - Chuyen tenant status sang `StatusChip.Success/Warning/Error`.
  - Chuyen tenant list cards sang `Card.Clickable`.
- `app/src/main/res/layout/fragment_tenant_detail.xml`:
  - Chuyen status tenant sang `StatusChip.Success`.
  - Dung `Card.Elevated` cho info card va `Card.State` cho room/billing cards.
  - Dung money typography cho room price.
- `app/src/main/res/layout/fragment_add_edit_tenant.xml`:
  - Dung semantic screen padding, form gap, elevated form card va `Button.Loading` cho save button.

### 6. Component da sua/tao

- Khong tao component Kotlin/XML include moi.
- Khong tao shared style/token moi.
- Chi ap dung lai shared components da co:
  - `Widget.SmartRent.Card.Clickable`
  - `Widget.SmartRent.Card.Elevated`
  - `Widget.SmartRent.Card.State`
  - `Widget.SmartRent.StatusChip.Info/Vacant/Warning/Success/Error`
  - `Widget.SmartRent.FilterChip`
  - `Widget.SmartRent.TextInputLayout.Search`
  - `Widget.SmartRent.TextInputEditText.Money`
  - `Widget.SmartRent.Button.Loading`

### 7. Token/shared components da ap dung

- Spacing/padding: `screen_padding_horizontal`, `screen_padding_vertical`, `screen_padding_bottom`, `section_spacing`, `component_gap`, `form_field_gap`, `card_padding`, `card_padding_compact`, `space_lg`.
- Color/status qua shared styles: `sr_info_container/text`, `sr_vacant_container/text`, `sr_warning_container/text`, `sr_success_container/text`, `sr_error_container/text`, `sr_filter_chip_background/content`.
- Typography: `TextAppearance.SmartRent.Money`, `TitleLarge`, `SectionTitle`, `Body`, `BodySecondary`, `Caption`.

### 8. Phan giu nguyen va ly do

- Cac Fragment Kotlin Room/Tenant: giu nguyen de khong doi click handling, validation, ViewBinding, UiState va navigation calls.
- `main_nav_graph.xml`: giu nguyen de khong doi route/navigation flow.
- `SmartRentMockData`, model/domain/repository/usecase/API: giu nguyen vi phase nay chi la UI upgrade.
- Billing/Invoice/Payment/Ticket/Notification/Profile/Settings: khong sua vi ngoai scope Phase 5.
- Avatar/initials rieng cho Tenant card: chua tao vi khong co component/avatar token call-site rieng trong module va brief cam tao style/data roi rac; nen uu tien status/list/card consistency truoc.

### 9. Breaking change va call-site

- Breaking change: khong co.
- Kotlin call-site phai sua: khong co.
- Cac id ViewBinding da giu:
  - Room list/detail/form: `addRoomButton`, `emptyAddRoomButton`, `room101Card`, `viewRoom101Button`, `room203Card`, `room305Card`, `editRoomButton`, `viewBillButton`, `addTenantButton`, `createBillButton`, `recordMeterButton`, `saveRoomButton`, `cancelButton`.
  - Tenant list/detail/form: `addTenantButton`, `emptyAddTenantButton`, `tenantMinhAnhCard`, `viewTenantMinhAnhButton`, `tenantLanHuongCard`, `tenantQuocBaoCard`, `editTenantButton`, `viewRoomButton`, `viewBillButton`, `createBillButton`, `transferRoomButton`, `endRentButton`, `saveTenantButton`, `cancelButton`.

### 10. Hard-code UI con lai

- Focused scan tren 6 layout Phase 5 khong con `bg_chip`, khong con `app:contentPadding="@dimen/space_*"` va khong con legacy `space_16/20/24/32` trong scope da sua.
- Cac spacing nho `space_4/8/12` van con trong noi dung card nhu gap noi bo hien huu; chua doi het de tranh churn layout qua rong.
- Cac module ngoai Room/Tenant van con `bg_chip` va legacy spacing tu phase truoc, nhung khong sua do ngoai scope.
- Static XML cards/mock data van con; chua model-driven Recycler/ListAdapter vi phase nay khong duoc doi data/business pattern.

### 11. Lenh build/check da chay

```powershell
.\gradlew.bat :app:assembleDebug
$files = 'fragment_rooms.xml','fragment_room_detail.xml','fragment_add_edit_room.xml','fragment_tenants.xml','fragment_tenant_detail.xml','fragment_add_edit_tenant.xml' | ForEach-Object { Join-Path 'app/src/main/res/layout' $_ }; Select-String -Path $files -Pattern 'bg_chip','app:contentPadding="@dimen/space_','space_16','space_20','space_24','space_32'
```

Ket qua:

- `BUILD SUCCESSFUL` trong 2s.
- Focused hard-code scan tren 6 layout Phase 5 khong tra ve ket qua.

### 12. Loi con lai

- Khong co loi build.
- Room/Tenant list van la static XML card, chua co reusable row component hoac adapter-driven list.
- Filter chips hien la UI tinh, chua co selected-state logic/filtering.
- Tenant card chua co avatar/initials component.

### 13. De xuat Phase 6

- Nang cap Billing/Invoice/Payment module theo cung token/shared component system.
- Chuyen status badges billing/payment sang `StatusChip.*`.
- Chuan hoa money typography, invoice summary card, payment state card va form spacing.
- Neu duoc phep doi logic sau UI phase, tach Room/Tenant/Billing list thanh reusable row/adapters va status mapper de giam static XML duplication.

## Phase 6 - Billing + Invoice + Payment

### 1. Ngay thuc hien

- 2026-06-23.

### 2. Tai lieu va skill da doc

- Da doc `docs/FE_Architecture.md`.
- Da doc `docs/SMARTRENT_MOBILE_UI_GUIDE.md`.
- Da doc `.agents/skills/design-taste-frontend/SKILL.md`.
- Da doc `.agents/skills/design-taste-frontend-v1/SKILL.md`.
- Da doc `.agents/skills/full-output-enforcement/SKILL.md`.
- Da doc `.agents/skills/gpt-taste/SKILL.md`.
- Da doc `.agents/skills/high-end-visual-design/SKILL.md`.
- Da doc `.agents/skills/imagegen-frontend-mobile/SKILL.md`.
- Da doc `.agents/skills/redesign-existing-projects/SKILL.md`.
- Da doc `.agents/skills/stitch-design-taste/SKILL.md`.
- Skill khong doc duoc: khong co.

Nguyen tac ap dung: giu XML/ViewBinding hien tai, khong doi navigation flow, khong sua backend/API/repository/usecase/domain, khong doi cong thuc tinh tien, khong doi validation/payment/invoice status logic, khong fake invoice/payment data moi, uu tien token Phase 1 va shared component styles Phase 2.

### 3. Kiem tra dieu kien bat dau

- Phase 1 log: co.
- Phase 2 log va Phase 2 resumed log: co.
- Phase 3 log: co.
- Phase 4 log: co.
- Phase 5 log: co.
- Theme/design tokens Phase 1: co.
- Shared components Phase 2: co va build duoc, gom `Widget.SmartRent.Card.Clickable`, `Widget.SmartRent.Card.Elevated`, `Widget.SmartRent.Card.State`, `Widget.SmartRent.StatusChip.*`, `Widget.SmartRent.FilterChip`, `Widget.SmartRent.TextInputEditText.Money`, `Widget.SmartRent.Button.Loading`.
- App Shell/Navigation UI Phase 3: co.
- Owner/Tenant Home Phase 4: co.
- Room/Tenant Module Phase 5: co.

### 4. Audit Billing/Invoice/Payment hien tai

- Repo dung Android XML + Fragment/ViewBinding cho scope nay; khong co Jetpack Compose trong Billing/Invoice/Payment.
- Khong co package `feature/billing`, `feature/invoice`, `feature/payment` rieng; module hien nam trong `app/src/main/java/com/example/rent/presentation/bills`.
- Screens hien co:
  - `BillsFragment.kt` + `fragment_bills.xml`: invoice/billing list dashboard, `UiState.Success(SmartRentMockData.bills)`.
  - `BillDetailFragment.kt` + `fragment_bill_detail.xml`: invoice detail, meter CTA, payment CTA, share disabled.
  - `AddEditBillFragment.kt` + `fragment_add_edit_bill.xml`: create/edit invoice mock form.
  - `MeterReadingFragment.kt` + `fragment_meter_reading.xml`: meter reading input + usage preview validation.
  - `PaymentRecordFragment.kt` + `fragment_payment_record.xml`: payment record form.
- ViewModel/Event/Effect rieng: khong co trong scope hien tai.
- UiState lien quan: chi co `UiState<List<BillUiModel>>` trong `BillsFragment`.
- Component rieng nhu `InvoiceCard`, `InvoiceStatusBadge`, `InvoiceFilterBar`, `InvoiceTimeline`, `PaymentStatusBadge`, `PaymentProofUploader`, `FeeBreakdownTable`: khong ton tai thanh file/component rieng; dang la static XML blocks.
- Invoice history, Billing rule, Share ratio, Upload payment proof, Payment detail/list rieng: khong co screen/route rieng trong repo hien tai.
- UI lech guide truoc phase:
  - Status badges dung `TextView` + `bg_chip` thu cong.
  - Invoice filters dung MaterialButton thay vi `FilterChip`.
  - Invoice cards tu set clickable/foreground/minHeight thay vi `Card.Clickable`.
  - Tong tien/gia tri tien chua dung money typography nhat quan.
  - Form tao bill/payment chua dung money input style.
  - Meter usage preview dung `bg_chip`, chua la state card.
  - Nhieu screen con legacy `space_16/20/24/32` va `app:contentPadding="@dimen/space_*"`.

### 5. Man hinh da nang cap

- `app/src/main/res/layout/fragment_bills.xml`:
  - Chuan hoa Billing/Invoice dashboard/list.
  - Dung `Card.Elevated` cho monthly receivable summary.
  - Dung `Card.State` cho overdue warning/error summary.
  - Chuyen filter buttons sang `Widget.SmartRent.FilterChip`.
  - Chuyen invoice status sang `StatusChip.Warning/Success/Error`.
  - Chuyen invoice item cards sang `Card.Clickable`.
  - Dung `TextAppearance.SmartRent.Money` cho cac so tien noi bat.
- `app/src/main/res/layout/fragment_bill_detail.xml`:
  - Chuyen status pending sang `StatusChip.Warning`.
  - Dung `Card.Elevated` cho tong tien dau man hinh.
  - Dung money typography cho tong tien va breakdown total.
  - Dung `Card.State` cho meter/payment sections.
- `app/src/main/res/layout/fragment_add_edit_bill.xml`:
  - Dung semantic screen padding, elevated form card, `form_field_gap`.
  - Dung `TextInputEditText.Money` cho rent/electric/water/fee/discount amount inputs.
  - Dung `Button.Loading` cho save CTA.
- `app/src/main/res/layout/fragment_meter_reading.xml`:
  - Dung semantic screen padding, elevated form card, `form_field_gap`.
  - Chuyen usage preview tu `bg_chip` sang `Card.State` voi info container.
  - Dung `Button.Loading` cho save CTA.
- `app/src/main/res/layout/fragment_payment_record.xml`:
  - Dung `Card.Elevated` cho invoice/payment summary va form card.
  - Dung money typography cho remaining amount.
  - Dung `TextInputEditText.Money` cho payment amount input.
  - Dung `Button.Loading` cho save CTA.

### 6. Component da sua/tao

- Khong tao component Kotlin/XML include moi.
- Khong tao shared style/token moi.
- Chi ap dung lai shared components da co:
  - `Widget.SmartRent.Card.Clickable`
  - `Widget.SmartRent.Card.Elevated`
  - `Widget.SmartRent.Card.State`
  - `Widget.SmartRent.StatusChip.Warning/Success/Error`
  - `Widget.SmartRent.FilterChip`
  - `Widget.SmartRent.TextInputEditText.Money`
  - `Widget.SmartRent.Button.Loading`

### 7. Token/shared components da ap dung

- Spacing/padding: `screen_padding_horizontal`, `screen_padding_vertical`, `screen_padding_bottom`, `section_spacing`, `component_gap`, `form_field_gap`, `card_padding`, `card_padding_compact`, `space_lg`.
- Color/status qua shared styles: warning cho pending/chua thanh toan, success cho paid/collected, error cho overdue.
- Typography: `TextAppearance.SmartRent.Money`, `TitleLarge`, `SectionTitle`, `Body`, `BodySecondary`, `Caption`.

### 8. Phan giu nguyen va ly do

- Cac Fragment Kotlin trong `presentation/bills`: giu nguyen de khong doi click handling, validation, mock navigation delay, UiState va disabled share behavior.
- `main_nav_graph.xml`: giu nguyen de khong doi route/navigation flow.
- `SmartRentMockData`, `BillUiModel`, domain/repository/usecase/API: giu nguyen vi Phase 6 chi upgrade UI.
- Cong thuc tinh tien, validation chi so dien nuoc, validation payment amount: giu nguyen trong `FormValidators`/Fragment hien co.
- Ticket/Notification/Profile/Settings/Community/Chat/Report: khong sua vi ngoai scope Phase 6.
- Billing rule, share ratio, invoice history, upload proof, payment detail/list rieng: khong tao moi vi repo chua co screen/route hien huu va brief cam them workflow/route moi.

### 9. Breaking change va call-site

- Breaking change: khong co.
- Kotlin call-site phai sua: khong co.
- Cac id ViewBinding da giu:
  - Billing/list: `createBillButton`, `emptyCreateBillButton`, `billPendingCard`, `viewBillPendingButton`, `billPaidCard`, `billOverdueCard`, `billsEmptyStateCard`.
  - Bill detail: `editBillButton`, `meterReadingButton`, `recordPaymentButton`, `shareBillButton`, `primaryPaymentButton`.
  - Add/Edit Bill: `formTitleTextView`, `billRoomInputLayout/EditText`, `billPeriodInputLayout/EditText`, `billRentAmountInputLayout/EditText`, `billElectricAmountInputLayout/EditText`, `billWaterAmountInputLayout/EditText`, `billFeeInputLayout/EditText`, `billDiscountInputLayout/EditText`, `billDueDateInputLayout/EditText`, `billNoteInputLayout/EditText`, `saveBillButton`, `cancelButton`.
  - Meter Reading: `meterRoomInputLayout/EditText`, `meterPeriodInputLayout/EditText`, `electricStartInputLayout/EditText`, `electricEndInputLayout/EditText`, `waterStartInputLayout/EditText`, `waterEndInputLayout/EditText`, `usagePreviewTextView`, `meterNoteInputLayout/EditText`, `saveMeterButton`, `cancelButton`.
  - Payment Record: `paymentAmountInputLayout/EditText`, `paymentMethodInputLayout/EditText`, `paymentDateInputLayout/EditText`, `paymentNoteInputLayout/EditText`, `savePaymentButton`, `cancelButton`.

### 10. Hard-code UI con lai

- Focused scan tren 5 layout Phase 6 khong con `bg_chip`, khong con `app:contentPadding="@dimen/space_*"` va khong con legacy `space_16/20/24/32`.
- Cac spacing nho `space_4/8/12` van con lam gap noi bo trong cards/buttons; chua doi het de tranh churn layout qua rong.
- Static XML invoice/payment cards/mock strings van con vi repo chua co adapter/component-driven invoice/payment UI va phase nay khong duoc doi data/business pattern.
- Cac module ngoai Billing/Invoice/Payment van con hard-code UI tu phase truoc, de lai cho Phase 7+.

### 11. Lenh build/check da chay

```powershell
.\gradlew.bat :app:assembleDebug
$files = 'fragment_bills.xml','fragment_bill_detail.xml','fragment_add_edit_bill.xml','fragment_meter_reading.xml','fragment_payment_record.xml' | ForEach-Object { Join-Path 'app/src/main/res/layout' $_ }; Select-String -Path $files -Pattern 'bg_chip','app:contentPadding="@dimen/space_','space_16','space_20','space_24','space_32'
```

Ket qua:

- `BUILD SUCCESSFUL` trong 2s.
- Focused hard-code scan tren 5 layout Phase 6 khong tra ve ket qua.

### 12. Loi con lai

- Khong co loi build.
- Billing/Invoice/Payment van la static XML + mock data; chua co ViewModel/StateFlow/repository-backed flow.
- Invoice status/filter chips hien la UI tinh, chua co selected-state/filtering logic.
- Chua co screen rieng cho Billing rule, Share ratio, Invoice history, Payment detail/list rieng, Upload payment proof.
- Chua co reusable `InvoiceCard`, `InvoiceStatusBadge`, `FeeBreakdownRow`, `PaymentProofPreview` components.

### 13. De xuat Phase 7: Ticket + Notification + Profile + Settings

- Nang cap Ticket/Notification/Profile/Settings theo token/shared component system.
- Chuyen status badges notification/profile/settings con dung `bg_chip` sang `StatusChip.*`.
- Chuan hoa card/list item/search/filter/empty states trong cac module nay.
- Sau Phase 7, co the lap phase rieng de tach static XML cards thanh reusable row/adapters va status mapper cho Room/Tenant/Billing/Notification.

## Phase 7 - Ticket + Notification + Profile + Settings

### 1. Ngay thuc hien

- 2026-06-23.

### 2. Tai lieu va skill da doc

- Da doc `docs/FE_Architecture.md`.
- Da doc `docs/SMARTRENT_MOBILE_UI_GUIDE.md`.
- Da doc `.agents/skills/design-taste-frontend/SKILL.md`.
- Da doc `.agents/skills/design-taste-frontend-v1/SKILL.md`.
- Da doc `.agents/skills/full-output-enforcement/SKILL.md`.
- Da doc `.agents/skills/gpt-taste/SKILL.md`.
- Da doc `.agents/skills/high-end-visual-design/SKILL.md`.
- Da doc `.agents/skills/imagegen-frontend-mobile/SKILL.md`.
- Da doc `.agents/skills/redesign-existing-projects/SKILL.md`.
- Da doc `.agents/skills/stitch-design-taste/SKILL.md`.
- Skill khong doc duoc: khong co. Cac path `skills/...` trong brief duoc resolve sang ban cai dat thuc te tai `.agents/skills/...`.

Nguyen tac tong hop ap dung: giu Android XML/ViewBinding hien tai, uu tien architecture va UI guide, khong tao route/feature moi, khong sua API/domain/repository/usecase/business logic, khong them fake production data, dung token Phase 1 va shared styles Phase 2, giu cac id ViewBinding dang duoc Kotlin su dung.

### 3. Kiem tra dieu kien bat dau

- Phase 1 log: co.
- Phase 2 resumed log: co.
- Phase 3 log: co.
- Phase 4 log: co.
- Phase 5 log: co.
- Phase 6 log: co.
- Theme/design tokens Phase 1: co.
- Shared components Phase 2: co va build duoc.
- App Shell/Navigation UI Phase 3: co.
- Owner/Tenant Home Phase 4: co.
- Room/Tenant Module Phase 5: co.
- Billing/Invoice/Payment Phase 6: co.

### 4. Audit Ticket/Notification/Profile/Settings hien tai

- Repo dang dung Android XML + Fragment/ViewBinding cho scope nay; khong co Compose trong cac man hinh Phase 7 hien huu.
- Ticket/Maintenance: khong co screen, Fragment, layout, route, ViewModel, UiState, component hay package feature trong `app/src/main`. Repo chi co token ticket trong `colors.xml` va mo ta architecture trong docs, nen Phase 7 khong tao moi ticket/maintenance UI de tranh them route/workflow ngoai scope hien co.
- Notification screens:
  - `NotificationsFragment.kt` + `fragment_notifications.xml`.
  - `NotificationDetailFragment.kt` + `fragment_notification_detail.xml`.
  - UiState hien tai la `UiState.Success(SmartRentMockData.notifications)`.
  - Filter va cards con tinh, mark-read/mark-all-read dang disabled vi chua co notification state store.
- Profile screen:
  - `ProfileFragment.kt` + `fragment_profile.xml`.
  - UiState hien tai la `UiState.Success(SmartRentMockData.profile)`.
  - Edit profile/help/policy/logout dang disabled placeholder theo Phase 2F.
- Settings screen:
  - `SettingsFragment.kt` + `fragment_settings.xml`.
  - Static settings UI; dark mode/logout/delete disabled, notification switches van la UI-only nhu truoc.
- UI lech guide truoc phase:
  - Notification filter dung button primary/secondary thay vi `FilterChip`.
  - Notification unread/read chip dung `bg_chip` thu cong.
  - Profile avatar/menu item dung `bg_chip`, menu note strings chua hien thi day du.
  - Settings row list con la text block don gian, chua nhat quan min height/gap theo row list mobile.
  - Mot so layout dung legacy spacing `space_16/24/32` va `app:contentPadding="@dimen/space_*"`.

### 5. Man hinh da nang cap

- `app/src/main/res/layout/fragment_notifications.xml`:
  - Chuyen filter buttons sang `ChipGroup` + `Widget.SmartRent.FilterChip`.
  - Chuyen unread badge sang `StatusChip.Info`.
  - Chuyen notification cards sang `Card.Clickable`.
  - Them cau truc row de scan nhanh: type marker, title 1 dong, body 2 dong, time/call-to-action.
  - Dung semantic spacing `screen_padding_*`, `section_spacing`, `form_field_gap`, `card_padding`, `component_gap`.
- `app/src/main/res/layout/fragment_notification_detail.xml`:
  - Chuyen type/status sang shared status chip/text token.
  - Dung `Card.Elevated` cho noi dung chinh va `Card.State` cho doi tuong lien quan.
  - Dung money typography cho so tien lien quan.
  - Giu CTA hien co den Bills/Rooms va mark-read disabled.
- `app/src/main/res/layout/fragment_profile.xml`:
  - Chuan hoa profile header bang elevated card, avatar token size, role chip.
  - Chuyen summary sang `Card.State` va semantic spacing.
  - Chuan hoa menu item thanh row touch target 64dp, hien thi note cho settings/notifications/help/policy.
  - Loai bo `bg_chip` khoi avatar/menu rows.
- `app/src/main/res/layout/fragment_settings.xml`:
  - Chuan hoa settings theo section ro: Tai khoan, Khong gian quan ly, Tuy chon ung dung, Thong bao, Phien lam viec.
  - Dung row list min height 64dp, card/state cards va semantic spacing.
  - Giu switch/select/app session controls hien co, khong them setting moi.
- `app/src/main/res/values/strings.xml`:
  - Them 4 string ngan cho notification type marker: bill, room, tenant, meter.

### 6. Component da sua/tao

- Khong tao component Kotlin/XML include moi.
- Khong tao shared style/token moi.
- Chi ap dung lai shared components da co:
  - `Widget.SmartRent.Card.Clickable`
  - `Widget.SmartRent.Card.Elevated`
  - `Widget.SmartRent.Card.State`
  - `Widget.SmartRent.StatusChip.Info`
  - `Widget.SmartRent.FilterChip`
  - `Widget.SmartRent.Button.Primary`
  - `Widget.SmartRent.Button.Secondary`
  - `Widget.SmartRent.Button.Text`

### 7. Token/shared components da ap dung

- Spacing/padding: `screen_padding_horizontal`, `screen_padding_vertical`, `screen_padding_bottom`, `section_spacing`, `component_gap`, `form_field_gap`, `card_padding`, `card_padding_compact`, `space_lg`, `space_xs`.
- Sizing: `list_item_min_height`, `avatar_size_md`, `avatar_size_lg`.
- Color/status: `sr_info_container/text`, `sr_vacant_container/text`, `sr_warning_container/text`, `sr_success_container/text`, `sr_neutral_container/text`, `sr_primary`.
- Typography: `TitleLarge`, `TitleMedium`, `SectionTitle`, `Body`, `BodySecondary`, `Caption`, `Label`, `Money`.

### 8. Phan giu nguyen va ly do

- Ticket/Maintenance: giu nguyen vi repo chua co screen/route/Fragment; khong tao moi de khong thay doi navigation flow hay business scope.
- Fragment Kotlin trong notifications/profile/settings: giu nguyen logic click, disabled actions, render state va mock UiState.
- `main_nav_graph.xml`: giu nguyen de khong doi route/navigation flow.
- `SmartRentMockData`, UiModel, domain/repository/usecase/API: giu nguyen vi Phase 7 chi upgrade UI.
- Edit profile/change password: khong co screen rieng trong repo hien tai, khong tao moi.
- Mark read/mark all read/logout/delete account/dark mode: van disabled/placeholder theo logic hien co.

### 9. Breaking change va call-site

- Breaking change: khong co.
- Kotlin call-site phai sua: khong co.
- Cac id ViewBinding da giu:
  - Notifications: `markAllReadButton`, `billNotificationCard`, `viewBillNotificationButton`, `roomNotificationCard`, `tenantNotificationCard`, `meterNotificationCard`.
  - Notification detail: `viewRelatedBillButton`, `viewRelatedRoomButton`, `markReadButton`.
  - Profile: `editProfileButton`, `settingsMenuItem`, `notificationsMenuItem`, `logoutButton`.
  - Settings: `profileInfoRow`, `changePasswordRow`, `darkModeSwitch`, `billNotificationSwitch`, `roomNotificationSwitch`, `tenantNotificationSwitch`, `systemNotificationSwitch`, `settingsLogoutButton`, `deleteAccountButton`.

### 10. Hard-code UI con lai

- Focused scan tren 4 layout Phase 7 khong con `bg_chip`, khong con `app:contentPadding="@dimen/space_*"` va khong con legacy `space_16/20/24/32`.
- Notification/Profile/Settings van la static XML + mock data tu phase truoc; chua co RecyclerView/adapter-driven list hay real API-backed state.
- Notification filter chips hien la UI tinh, chua co selected-state filtering logic.
- Notification type marker hien dung text ngan tu resource string, chua co icon asset rieng.
- Cac module ngoai scope Phase 7 co the van con hard-code UI tu phase truoc.

### 11. Lenh build/check da chay

```powershell
Select-String -Path 'app/src/main/res/layout/fragment_notifications.xml','app/src/main/res/layout/fragment_notification_detail.xml','app/src/main/res/layout/fragment_profile.xml','app/src/main/res/layout/fragment_settings.xml' -Pattern 'bg_chip','app:contentPadding="@dimen/space_','space_16','space_20','space_24','space_32'
.\gradlew.bat :app:assembleDebug
rg -n "ticket|maintenance|Ticket|Maintenance" app/src/main -g "*.kt" -g "*.xml"
```

Ket qua:

- Focused hard-code scan tren 4 layout Phase 7 khong tra ve ket qua.
- `BUILD SUCCESSFUL`.
- Ticket/Maintenance scan trong `app/src/main` chi thay token mau/strings maintenance cua Rooms, khong thay ticket/maintenance screen implementation.

### 12. Loi con lai

- Khong co loi build.
- Khong co Git metadata trong workspace (`git status` bao `not a git repository`), nen khong lay duoc git diff/status.
- Ticket/Maintenance module chua ton tai trong app source.
- Profile edit, change password, mark notification read, logout/delete va settings persistence chua co logic/screen thuc.

### 13. De xuat Phase 8: Polish + Final UI Consistency Check

- Kiem tra toan app de loai bo `bg_chip`, legacy spacing va duplicate static card pattern con lai ngoai scope Phase 7.
- Neu duoc phep cham architecture UI, tach list static sang adapter/model-driven rows cho Notifications, Rooms, Tenants, Bills.
- Tao icon/type component dung chung cho Notification va Ticket khi Ticket module that su duoc them.
- QA lai small screen, large font, TalkBack labels va disabled action affordance tren Auth/Home/Room/Tenant/Billing/Notification/Profile/Settings.

## Phase 8 - Polish + Final UI Consistency Check

Ngay thuc hien: 2026-06-23

### 1. Tai lieu va skill da doc

- `docs/FE_Architecture.md`: xac nhan app Android Native XML + Kotlin/ViewBinding, giu MVVM, navigation hien co va khong them API/domain/repository/usecase trong phase nay.
- `docs/SMARTRENT_MOBILE_UI_GUIDE.md`: dung token/shared component system hien co lam nguon uu tien cho spacing, typography, card, chip, button, form va state UI.
- `.agents/skills/redesign-existing-projects/SKILL.md`: dung de audit va polish UI hien co thay vi rebuild.
- `.agents/skills/design-taste-frontend/SKILL.md`
- `.agents/skills/design-taste-frontend-v1/SKILL.md`
- `.agents/skills/full-output-enforcement/SKILL.md`
- `.agents/skills/gpt-taste/SKILL.md`
- `.agents/skills/high-end-visual-design/SKILL.md`
- `.agents/skills/imagegen-frontend-mobile/SKILL.md`
- `.agents/skills/stitch-design-taste/SKILL.md`

Ket qua: tat ca file skill bat buoc doc duoc, khong co skill missing/unreadable.

### 2. Xac nhan prerequisite Phase 1-7

- `docs/frontend_phase_change_log.md` co log Phase 1 den Phase 7.
- Phase 7 da ghi ro Ticket/Maintenance chua ton tai trong `app/src/main`, nen Phase 8 khong tao screen/route moi.
- App Shell, Auth, Home, Rooms, Tenants, Bills/Payment/Meter, Notifications, Profile va Settings da co UI XML hien huu de audit.

### 3. Pham vi Phase 8 da giu

- Khong them feature moi.
- Khong them route/navigation flow moi.
- Khong sua API/domain/repository/usecase/business logic.
- Khong them fake data moi.
- Khong migrate XML sang Compose.
- Khong refactor Kotlin/ViewModel/UiState.
- Chi polish UI XML/token drift va sua loi lint resource/config nho can thiet de final check pass.

### 4. Audit UI toan app

- Layout hien co: `activity_main.xml` va 20 fragment XML trong `app/src/main/res/layout`.
- Bottom nav/topbar/app shell: giu `Widget.SmartRent.BottomNavigation`, khong doi navigation graph.
- Forms: Auth, Room, Tenant, Bill, Meter, Payment dang dung `Widget.SmartRent.TextInputLayout`, `Widget.SmartRent.TextInputEditText` va `Widget.SmartRent.Button.*`.
- Cards/lists: Home, Rooms, Tenants, Billing, Notification, Profile, Settings da dung `Widget.SmartRent.Card.*` va semantic padding.
- Status badges/filter chips: Rooms/Tenants/Bills/Notifications da dung `Widget.SmartRent.StatusChip.*` hoac `Widget.SmartRent.FilterChip`; khong con layout call-site dung `bg_chip`.
- Loading/empty/error: cac screen hien co giu static mock UI va disabled/placeholder logic tu phase truoc; khong them adapter/API state.
- Community/Chat/Report: khong co screen, Fragment, layout, route hay package trong `app/src/main`.
- Ticket: chi con token mau `sr_ticket_*` trong `colors.xml`; khong co Ticket screen/route/module trong `app/src/main`.

### 5. Thay doi da thuc hien

- `app/src/main/res/layout/fragment_login.xml`
  - Chuyen outer vertical padding sang `screen_padding_vertical`/`screen_padding_bottom`.
  - Chuyen card spacing sang `section_spacing`.
  - Chuyen prompt/support spacing tu legacy `space_16` sang `space_lg`.
- `app/src/main/res/layout/fragment_register.xml`
  - Chuyen outer vertical padding sang semantic screen padding.
  - Chuyen card/action spacing tu legacy `space_24`/`space_20`/`space_16` sang `section_spacing`/`space_lg`.
- `app/src/main/res/layout/fragment_forgot_password.xml`
  - Chuyen outer vertical padding sang semantic screen padding.
  - Chuyen card/action/state/back spacing sang `section_spacing`/`space_lg`.
- `app/src/main/res/layout/fragment_auth.xml`
  - Tach `android:padding` legacy thanh screen padding start/top/end/bottom.
  - Chuyen compact card padding sang `card_padding_compact`.
- `app/src/main/res/values/themes.xml`
  - Them `tools:targetApi="27"` cho `android:windowLightNavigationBar` de lint biet item nay co chu dich API 27+.
- `app/src/main/res/values-night/themes.xml`
  - Them `tools:targetApi="27"` cho `android:windowLightNavigationBar`.
- `app/src/main/res/values/strings.xml`
  - Sua literal percent trong `home_hint_month_revenue` tu `+12%` thanh `+12%%` de hop le Android string formatting.
- `gradle.properties`
  - Escape drive colon trong `org.gradle.java.home` thanh `C\:\\...` de lint property parser hop le tren Windows.

### 6. Component/token da ap dung

- Spacing: `screen_padding_horizontal`, `screen_padding_vertical`, `screen_padding_bottom`, `section_spacing`, `space_lg`.
- Card padding: `card_padding`, `card_padding_compact`.
- Button/form/card styles giu nguyen shared system: `Widget.SmartRent.Button.Loading`, `Widget.SmartRent.Button.Text`, `Widget.SmartRent.Card`, `Widget.SmartRent.Card.Compact`, `Widget.SmartRent.Card.State`, `Widget.SmartRent.TextInputLayout`, `Widget.SmartRent.TextInputEditText`.
- Typography giu `TextAppearance.SmartRent.*`; khong them text style moi.

### 7. Hard-code UI scan sau polish

```powershell
rg -n -e 'bg_chip' -e 'app:contentPadding="@dimen/space_' -e '@dimen/space_16' -e '@dimen/space_20' -e '@dimen/space_24' -e '@dimen/space_32' . # workdir: app/src/main/res/layout
rg -n -e '#[0-9A-Fa-f]{6,8}' -e 'android:textSize="[0-9]+sp"' -e 'android:padding[A-Za-z]*="[0-9]+dp"' -e 'android:layout_margin[A-Za-z]*="[0-9]+dp"' -e 'app:cardCornerRadius="[0-9]+dp"' -e 'app:cardElevation="[0-9]+dp"' -e 'app:contentPadding="[0-9]+dp"' . # workdir: app/src/main/res/layout
rg -n -e 'fragment_(community|chat|report)' -e 'Community' -e 'Chat' -e 'Report' -e 'ticket' -e 'Ticket' . # workdir: app/src/main
```

Ket qua:

- Layout scan khong tra ve ket qua cho `bg_chip`, `app:contentPadding="@dimen/space_*"`, legacy `space_16/20/24/32`, raw hex, raw `sp`, raw margin/padding `dp`, raw card radius/elevation/content padding.
- Community/Chat/Report khong co source screen.
- Ticket scan chi thay `sr_ticket_*` token trong `colors.xml`, khong thay screen/route/module.
- Launcher vector va color token files co raw hex theo dung vai tro resource/token, khong phai layout drift.

### 8. Lenh build/check da chay

```powershell
.\gradlew.bat :app:assembleDebug
.\gradlew.bat :app:lintDebug --console=plain
.\gradlew.bat :app:testDebugUnitTest --console=plain
```

Ket qua:

- `:app:assembleDebug`: `BUILD SUCCESSFUL`.
- `:app:lintDebug`: ban dau fail 4 loi (`windowLightNavigationBar` API lint x2, escaped Windows property, invalid `%` string); da sua va rerun `BUILD SUCCESSFUL`.
- `:app:testDebugUnitTest`: `BUILD SUCCESSFUL`.

### 9. Breaking change va call-site

- Breaking change: khong co.
- Kotlin/ViewBinding call-site phai sua: khong co.
- Navigation graph: khong sua.
- Fragment IDs: giu nguyen.
- API/domain/repository/usecase/mock data: khong sua.

### 10. Loi con lai / gioi han

- Lint pass nhung report van co warning non-blocking, gom unused legacy resource nhu `bg_chip`; khong xoa de tranh pha compatibility cua resource cu neu phase sau con can.
- App van la static XML/mock UI theo scope Phase 1-8, chua co API-backed loading/empty/error thuc.
- Ticket/Maintenance/Community/Chat/Report chua co source screen nen khong polish duoc trong Phase 8.
- Khong co Git metadata trong workspace (`git status` bao `not a git repository`), nen khong lay duoc git diff/status.

### 11. Ket luan Phase 8

Final UI consistency status: PASS.

Ly do PASS: tat ca docs/skills bat buoc doc duoc, Phase 1-7 log co mat, app-wide implemented layout scan sach voi cac drift pattern muc tieu, khong them navigation/feature/logic moi, va `assembleDebug`, `lintDebug`, `testDebugUnitTest` deu pass.

---

## 2026-06-25 - Fix bottom navigation overlap va safe area

### Van de da fix

- Bottom navigation tab item bi de icon len label o cac tab chinh: `Tong quan`, `Phong`, `Nguoi thue`, `Hoa don`, `Ca nhan`.
- Bottom navigation chua cong safe area bottom rieng khi app chay edge-to-edge, de gay cam giac dinh sat gesture bar.
- Noi dung scroll o cac man chinh co nguy co bi khu sau bottom navigation, dac biet man `Ca nhan`.

### File da sua

- `app/src/main/java/com/example/rent/MainActivity.kt`
  - Tach WindowInsets bottom ra khoi root padding.
  - Tang chieu cao bottom nav theo `bottom_nav_height + systemBars.bottom`.
  - Cong `systemBars.bottom` vao padding bottom cua bottom nav.
- `app/src/main/res/values/dimens.xml`
  - Them token `bottom_nav_icon_size`, `bottom_nav_item_padding_top`, `bottom_nav_item_padding_bottom`, `main_content_bottom_padding`.
- `app/src/main/res/values/styles.xml`
  - Them text appearance rieng cho bottom nav active/inactive.
  - Set icon 24dp, label 11sp, padding doc on dinh, tat horizontal translation.
- `app/src/main/res/layout/fragment_home.xml`
- `app/src/main/res/layout/fragment_rooms.xml`
- `app/src/main/res/layout/fragment_tenants.xml`
- `app/src/main/res/layout/fragment_bills.xml`
- `app/src/main/res/layout/fragment_profile.xml`
  - Them `android:paddingBottom="@dimen/main_content_bottom_padding"` tren root `NestedScrollView`.
  - Giu `android:clipToPadding="false"` de item cuoi scroll len tren nav.

### Cach kiem tra

- Build/lint/unit test:
  - `.\gradlew.bat assembleDebug lintDebug testDebugUnitTest`
  - `.\gradlew.bat installDebug`
- Emulator QA:
  - Mo app tren `emulator-5554`.
  - Login mock bang email/password hop le.
  - Tap lan luot 5 tab bottom nav: `Tong quan`, `Phong`, `Nguoi thue`, `Hoa don`, `Ca nhan`.
  - Dump UI tree bang `adb exec-out uiautomator dump /dev/tty`.
  - Kiem tra man `Ca nhan` sau khi scroll xuong cuoi.
  - Screenshot kiem tra: `profile-bottom-nav-check.png`.

### Ket qua build/test

- `assembleDebug`: PASS.
- `lintDebug`: PASS.
- `testDebugUnitTest`: PASS.
- `installDebug`: PASS tren `emulator-5554`.
- UI tree 5 tab: moi tab selected dung khi tap.
- Bottom nav: icon va label tach rieng; label khong overlap icon.
- Safe area: bottom nav nam tren gesture bar, view con cua nav ket thuc o y=2361 va nav container keo den y=2424.
- Man `Ca nhan`: item cuoi/ghi chu logout ket thuc o y=1899, bottom nav bat dau o y=2193, khong bi che.
- Navigation graph/route/API: khong sua.

---

## 2026-06-25 - Fix Profile Vietnamese copy va layout row thao tac

### Loi da fix

- Man `Ca nhan/Profile` hien thi nhieu copy tieng Viet khong dau: `Tai khoan...`, `Thong tin...`, `Cai dat...`, `Dang xuat...`.
- Section `Tai khoan va thao tac` bi vo rhythm: title va subtitle cua item la cac `TextView` tach roi, title co `minHeight`, lam subtitle bi day xa va nhin nhu nam lung tung.
- Font family token dang dung `sans`; da chuan hoa sang `sans-serif` de Android dung family he thong ho tro tieng Viet day du.

### File da sua

- `app/src/main/res/values/strings.xml`
  - Chuan hoa toan bo copy Profile sang tieng Viet co dau UTF-8.
  - Cap nhat dung cac copy yeu cau: `Ca nhan`, `Tai khoan chu nha...`, `Sua ho so`, `Tom tat tai khoan`, `Dang quan ly`, `Tong phong`, `Nguoi thue`, `Cho thue`, `Tai khoan va thao tac`, `Thong tin tai khoan`, `Cai dat ung dung`, `Trung tam thong bao`.
- `app/src/main/res/layout/fragment_profile.xml`
  - Gom moi item trong section thao tac thanh mot row vertical.
  - Title/subtitle cung can trai, padding doc deu, subtitle dung `BodySmall` va `lineSpacingExtra`.
  - Bo `minHeight` tren title rieng le; them divider giua cac row.
- `app/src/main/res/values/styles.xml`
  - Doi shared text appearances tu `sans` sang `sans-serif`.
- `app/src/main/res/values/themes.xml`
- `app/src/main/res/values-night/themes.xml`
  - Doi theme font family tu `sans` sang `sans-serif`.

### Nguyen nhan

- Copy Profile trong `strings.xml` la ban khong dau nen emulator render dung resource nhung text trong app mat tu nhien.
- Layout cu dat title/subtitle la cac sibling doc lap; title co `android:minHeight="@dimen/list_item_min_height"` nen subtitle bi tach khoi title, tao khoang trong lon khong deu.
- `sans` khong ro rang bang `sans-serif` trong Android font family; viec chuan hoa giup font fallback he thong on dinh hon voi dau tieng Viet.

### Cach kiem tra

- Doc lai `docs/FE_Architecture.md`, `docs/SMARTRENT_MOBILE_UI_GUIDE.md`, va `.agents`.
- Build/lint/unit test:
  - `.\gradlew.bat assembleDebug lintDebug testDebugUnitTest`
  - `.\gradlew.bat installDebug`
- Emulator QA tren `emulator-5554`:
  - Force-stop va mo app.
  - Login mock bang `test@example.com` / `password`.
  - Tap tab `Ca nhan`.
  - Dump UI tree bang `adb exec-out uiautomator dump /dev/tty`.
  - Scroll xuong section `Tai khoan va thao tac`.
  - Chup screenshot: `profile-fixed-check.png`.

### Ket qua build/test

- `assembleDebug`: PASS.
- `lintDebug`: PASS.
- `testDebugUnitTest`: PASS.
- `installDebug`: PASS tren `emulator-5554`.
- UI tree xac nhan tab `Ca nhan` selected dung.
- UI tree xac nhan cac copy chinh hien thi dung dau: `Tai khoan chu nha, vai tro va cac loi tat cai dat.`, `Sua ho so`, `Tom tat tai khoan`, `Dang quan ly`, `Tong phong`, `Nguoi thue`, `Cho thue`, `Tai khoan va thao tac`, `Thong tin tai khoan`, `Cai dat ung dung`, `Trung tam thong bao`.
- Screenshot `profile-fixed-check.png` xac nhan row trong `Tai khoan va thao tac` thang hang, spacing deu, subtitle khong bi cat/chong chu.
- Noi dung cuoi man Profile van nam tren bottom navigation, khong bi che.
- API/navigation graph/route: khong sua.
