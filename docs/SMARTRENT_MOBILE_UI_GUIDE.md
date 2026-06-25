# SMARTRENT MOBILE UI GUIDE

> Hệ thống thiết kế giao diện cho ứng dụng quản lý nhà/phòng cho thuê (mobile)
> Dành cho chủ trọ & người thuê — quản lý phòng, hợp đồng, hóa đơn, thanh toán, sự cố
>
> Tổng hợp định hướng từ Stripe (rõ ràng, tin cậy, dữ liệu tài chính), Retool (mật độ thông tin cao, bảng/danh sách), Linear (tốc độ, độ chính xác, trạng thái), Airbnb (thân thiện, ấm áp, hình ảnh) — chọn lọc và điều chỉnh cho bối cảnh quản lý nhà trọ thực tế tại Việt Nam.

---

## Mục lục

1. [Định hướng thiết kế tổng thể](#1-định-hướng-thiết-kế-tổng-thể)
2. [Bảng màu](#2-bảng-màu)
3. [Typography](#3-typography)
4. [Khoảng cách & Bo góc](#4-khoảng-cách--bo-góc)
5. [Độ sâu / Elevation](#5-độ-sâu--elevation)
6. [Layout Mobile & Navigation](#6-layout-mobile--navigation)
7. [Thư viện Component](#7-thư-viện-component)
8. [Các màn hình chính](#8-các-màn-hình-chính)
9. [Form nhập liệu](#9-form-nhập-liệu)
10. [Bảng / Danh sách dữ liệu](#10-bảng--danh-sách-dữ-liệu)
11. [Trạng thái dữ liệu (Status)](#11-trạng-thái-dữ-liệu-status)
12. [Empty State](#12-empty-state)
13. [Loading State](#13-loading-state)
14. [Animation & Tương tác](#14-animation--tương-tác)
15. [Việc nên làm / không nên làm](#15-việc-nên-làm--không-nên-làm)

---

## 1. Định hướng thiết kế tổng thể

Ứng dụng phục vụ hai vai trò chính:

- **Chủ trọ**: cần xem nhanh tình trạng nhà trọ (phòng trống/đã thuê), theo dõi công nợ, hóa đơn, hợp đồng sắp hết hạn — giao diện cần **mật độ thông tin cao nhưng rõ ràng**, giống cách Retool/Linear trình bày danh sách và trạng thái.
- **Người thuê**: cần xem hóa đơn, thanh toán, gửi phản ánh sự cố — giao diện cần **thân thiện, dễ thao tác bằng một tay**, gần với cảm giác ấm áp, đáng tin của Airbnb.

**Nguyên tắc chủ đạo:**

- **Rõ ràng & tin cậy trước tiên**: vì app xử lý tiền (hóa đơn, thanh toán), ưu tiên độ tương phản tốt, số liệu dễ đọc, trạng thái thanh toán không thể gây nhầm lẫn (lấy cảm hứng từ độ rõ ràng tài chính của Stripe).
- **Mật độ vừa phải cho mobile**: không dày đặc như builder của Retool (đó là cho desktop chuyên gia), nhưng vẫn ưu tiên hiển thị nhiều dữ liệu hữu ích trên một màn hình nhỏ.
- **Trạng thái là công dân hạng nhất**: phòng trống/đã thuê, hóa đơn đã thanh toán/quá hạn, sự cố mới/đang xử lý/đã xong — luôn thể hiện bằng màu sắc + nhãn rõ ràng, theo cách Linear xử lý priority/status.
- **Thân thiện nhưng chuyên nghiệp**: bo góc vừa phải, màu sắc ấm nhưng không lòe loẹt, không cần ảnh chụp lớn chiếm trọn màn hình như Airbnb — đây là app quản lý, không phải app khám phá.
- **Một tay, ngón cái**: vùng chạm chính (nút thêm hóa đơn, xác nhận thanh toán, tab bar) nằm trong tầm với ngón cái ở nửa dưới màn hình.

---

## 2. Bảng màu

### Màu thương hiệu (Primary)

| Token | Hex | Vai trò |
|-------|-----|---------|
| `color-brand` | `#2F6FED` | Hành động chính (nút CTA), liên kết, tab đang chọn |
| `color-brand-dark` | `#2557C7` | Trạng thái hover/pressed của brand |
| `color-brand-light` | `#EAF1FE` | Nền nhạt cho badge/chip liên quan đến brand |

> Chọn xanh dương trung tính thay vì cam/tím để truyền tải sự *tin cậy tài chính* (giống tinh thần Stripe) nhưng vẫn đủ "mềm" cho một app đời sống hàng ngày.

### Màu trung tính (Neutral)

| Token | Hex | Vai trò |
|-------|-----|---------|
| `color-bg` | `#F7F8FA` | Nền màn hình chính |
| `color-surface` | `#FFFFFF` | Nền card, sheet, modal |
| `color-border` | `#E5E8EC` | Viền, đường phân cách |
| `color-text-primary` | `#1A1D23` | Tiêu đề, số liệu quan trọng |
| `color-text-secondary` | `#6B7280` | Mô tả, nhãn phụ |
| `color-text-muted` | `#9CA3AF` | Placeholder, vô hiệu hóa |

### Màu trạng thái (Semantic)

| Token | Hex | Dùng cho |
|-------|-----|----------|
| `color-success` | `#16A34A` | Đã thanh toán, hợp đồng còn hiệu lực, phòng đang thuê ổn định |
| `color-warning` | `#F59E0B` | Sắp đến hạn thanh toán, hợp đồng sắp hết hạn, sự cố đang xử lý |
| `color-error` | `#DC2626` | Quá hạn thanh toán, sự cố khẩn cấp, hành động xóa/hủy |
| `color-info` | `#2F6FED` | Thông báo chung, gợi ý |
| `color-vacant` | `#0EA5A4` | Trạng thái phòng trống (màu teal, tách biệt với success/error) |

### Nguyên tắc dùng màu

- Mỗi màn hình chỉ dùng **tối đa 1 màu nhấn chính** (`color-brand`) cho hành động chính; các màu còn lại chỉ phục vụ trạng thái dữ liệu.
- Không dùng `color-error` cho bất cứ thứ gì ngoài cảnh báo/lỗi/hành động phá hủy — người dùng phải nhìn màu đỏ là biết "cần chú ý ngay".
- Trạng thái phòng/hóa đơn luôn đi kèm **chip màu nền nhạt + chữ màu đậm cùng tông** (ví dụ nền `#FEF3C7` + chữ `#92400E` cho warning) để đảm bảo độ tương phản và dễ quét mắt trong danh sách dài.

---

## 3. Typography

### Font

- **Chính**: Inter (rõ ràng, hỗ trợ tiếng Việt tốt, đã được kiểm chứng qua nhiều app fintech/SaaS)
- **Số liệu/mã**: một font có số đều (tabular figures) — dùng Inter với `font-variant-numeric: tabular-nums` cho số tiền, mã phòng, mã hóa đơn để các cột số thẳng hàng trong danh sách.

### Thang chữ (Mobile)

| Vai trò | Size | Weight | Line height | Dùng cho |
|---------|------|--------|-------------|----------|
| Display | 28px | 700 | 1.2 | Số liệu tổng quan lớn (tổng doanh thu tháng) |
| H1 | 22px | 700 | 1.25 | Tiêu đề màn hình |
| H2 | 18px | 600 | 1.3 | Tiêu đề section trong màn hình |
| H3 | 16px | 600 | 1.4 | Tiêu đề card, tên phòng/người thuê |
| Body | 15px | 400 | 1.5 | Nội dung chính |
| Body Small | 13px | 400 | 1.5 | Mô tả phụ, ghi chú |
| Label | 12px | 500 | 1.3 | Nhãn field, badge, tag |
| Số tiền | 18px | 700 | 1.2 | Số tiền hóa đơn (luôn đậm, nổi bật) |
| Caption | 11px | 400 | 1.4 | Mốc thời gian, metadata nhỏ nhất |

**Nguyên tắc:**

- Số tiền luôn đậm hơn (weight 700) và lớn hơn các text xung quanh — đây là thông tin người dùng quét tìm đầu tiên.
- Không để body text nhỏ hơn 13px trên mobile.
- Tên phòng / mã phòng dùng weight 600 để dễ phân biệt trong danh sách dày.

---

## 4. Khoảng cách & Bo góc

### Spacing scale

| Token | Giá trị | Dùng cho |
|-------|---------|----------|
| `space-1` | 4px | Khoảng cách icon-text |
| `space-2` | 8px | Khoảng cách giữa các phần tử trong 1 hàng |
| `space-3` | 12px | Padding trong card nhỏ, gap giữa field form |
| `space-4` | 16px | Padding chuẩn của card, margin ngang màn hình |
| `space-5` | 20px | Khoảng cách giữa các nhóm trong 1 section |
| `space-6` | 24px | Khoảng cách giữa các section |
| `space-8` | 32px | Padding đầu/cuối màn hình, khoảng cách lớn |

> Margin ngang mặc định toàn app: **16px** hai bên — đây là chuẩn chạm an toàn và dễ đọc trên hầu hết kích thước màn hình điện thoại.

### Bo góc

| Token | Giá trị | Dùng cho |
|-------|---------|----------|
| `radius-sm` | 6px | Badge, chip trạng thái, tag |
| `radius-md` | 10px | Input, button |
| `radius-lg` | 14px | Card (phòng, hóa đơn, người thuê) |
| `radius-xl` | 20px | Bottom sheet, modal |
| `radius-full` | 9999px | Avatar, pill trạng thái, FAB |

---

## 5. Độ sâu / Elevation

| Mức | Giá trị | Dùng cho |
|-----|---------|----------|
| `shadow-card` | `0 1px 3px rgba(16,24,40,0.06)` | Card ở trạng thái nghỉ |
| `shadow-raised` | `0 4px 12px rgba(16,24,40,0.10)` | Card được nhấn/kéo, FAB |
| `shadow-sheet` | `0 -4px 24px rgba(16,24,40,0.12)` | Bottom sheet, modal kéo lên |
| `shadow-nav` | `0 -1px 0 rgba(16,24,40,0.06)` | Thanh tab bar dưới cùng |

Giữ shadow nhẹ, không dùng shadow nặng kiểu modal desktop — trên mobile, phân tách bằng **nền + viền mỏng** thường hiệu quả hơn shadow đậm.

---

## 6. Layout Mobile & Navigation

### Cấu trúc khung ứng dụng

- **Thanh điều hướng dưới (Bottom Tab Bar)** — cố định, cao 56–64px, tối đa **5 tab**:
  1. Trang chủ / Tổng quan
  2. Phòng
  3. Hóa đơn (có badge số lượng chưa thanh toán/quá hạn)
  4. Thông báo
  5. Cá nhân / Cài đặt
- **Header màn hình**: cao 56px, tiêu đề căn trái (không center) để tận dụng không gian cho icon hành động bên phải (tìm kiếm, thêm mới, lọc).
- **Floating Action Button (FAB)**: dùng cho hành động tạo nhanh theo ngữ cảnh (ví dụ "+ Tạo hóa đơn" ở màn hình Hóa đơn, "+ Thêm phòng" ở màn hình Phòng) — góc dưới phải, kích thước 56×56px, `radius-full`, màu `color-brand`.

### Lưới & container

- Danh sách phòng dạng **lưới 2 cột** trên điện thoại thường (grid card có ảnh/icon trạng thái), chuyển sang **danh sách 1 cột** khi cần hiển thị nhiều thông tin (hóa đơn, hợp đồng).
- Khoảng cách lưới (gutter): 12px.
- Vùng nội dung cuộn được đặt padding-bottom đủ lớn để không bị tab bar che (tối thiểu 80px).

### Điều hướng theo chiều sâu

- Từ danh sách → chi tiết: chuyển trang trượt ngang (push), có nút back rõ ràng ở header trái.
- Form tạo/sửa (thêm phòng, tạo hóa đơn, thêm người thuê): mở dạng **bottom sheet kéo lên toàn màn hình** hoặc modal full-screen, có nút "Đóng" (X) góc trái và "Lưu" góc phải trong header.

### Vùng chạm (Touch target)

- Tối thiểu **44×44px** cho mọi phần tử có thể chạm (nút, icon, hàng danh sách).
- Hàng danh sách (row) trong bảng dữ liệu cao tối thiểu 56px để đảm bảo dễ chạm dù hiển thị nhiều thông tin.

---

## 7. Thư viện Component

### Button

| Loại | Style |
|------|-------|
| Primary | Nền `color-brand`, chữ trắng, radius 10px, padding `14px 20px`, font 15px/600 |
| Secondary | Nền trắng, viền `color-border` 1px, chữ `color-text-primary` |
| Danger | Nền `color-error`, chữ trắng — chỉ dùng cho hành động xóa/hủy hợp đồng |
| Ghost / Text | Không nền, không viền, chữ `color-brand` — dùng trong card, hàng danh sách |
| Trạng thái | Disabled: opacity 40%. Pressed: tối màu nền 8%. Loading: spinner thay icon, giữ nguyên độ rộng |

### Input

- Viền `color-border` 1px, radius 10px, padding `12px 14px`, font 15px.
- Focus: viền `color-brand`, ring `rgba(47,111,237,0.15)`.
- Lỗi: viền `color-error`, text lỗi 13px màu `color-error` ngay dưới field.
- Input số tiền: căn phải, font tabular-nums, hiển thị đơn vị "đ" cố định bên phải.

### Card

**Card phòng**
- Nền trắng, radius 14px, shadow-card, padding 14px.
- Góc trên: ảnh/thumbnail phòng (hoặc icon mặc định) + chip trạng thái (Trống / Đang thuê / Đang sửa) góc phải ảnh.
- Thân: mã phòng (H3), diện tích/loại phòng (Body Small), giá thuê (Số tiền).
- Chân: tên người thuê hiện tại + avatar tròn nhỏ (nếu đang thuê).

**Card hóa đơn**
- Hàng ngang: tên phòng/người thuê bên trái, số tiền + chip trạng thái thanh toán bên phải.
- Dòng phụ: kỳ hóa đơn (VD "Tháng 6/2026"), hạn thanh toán.

**Card thông báo / sự cố**
- Icon loại sự cố bên trái, tiêu đề + mô tả ngắn ở giữa, thời gian + chip trạng thái bên phải.

### Badge / Chip trạng thái

- Bo góc `radius-full`, padding `4px 10px`, font Label (12px/500).
- Nền nhạt + chữ đậm cùng tông màu (xem mục 11).

### Avatar

- Tròn, kích thước chuẩn 36px (danh sách) / 56px (chi tiết), viền mỏng `color-border` nếu trên nền ảnh.

### Bottom Sheet / Modal

- Bo góc trên `radius-xl` (20px), thanh kéo (drag handle) 36×4px màu `color-border` ở giữa trên cùng.
- Header trong sheet: tiêu đề trái, nút đóng phải.

### Tab Bar (dưới)

- Nền trắng, `shadow-nav`, icon 24px + label 11px.
- Tab đang chọn: icon + label màu `color-brand`; các tab khác màu `color-text-muted`.
- Badge số đỏ (`color-error`) góc trên-phải icon khi có hóa đơn quá hạn/thông báo chưa đọc.

---

## 8. Các màn hình chính

### 8.1 Trang chủ / Tổng quan (Chủ trọ)

- Khối số liệu tổng quan trên cùng (dạng 2×2 hoặc carousel ngang): tổng số phòng, phòng trống, doanh thu tháng, hóa đơn quá hạn.
- Danh sách "Cần xử lý" (hóa đơn quá hạn, hợp đồng sắp hết hạn, sự cố mới) — ưu tiên hiển thị theo mức độ khẩn cấp (màu warning/error trước).
- Section "Hoạt động gần đây".

### 8.2 Danh sách Phòng

- Thanh tìm kiếm + bộ lọc (theo trạng thái: Tất cả / Trống / Đang thuê / Đang sửa) dạng chip cuộn ngang ngay dưới header.
- Lưới card phòng 2 cột (xem mục 7).
- FAB "+ Thêm phòng".

### 8.3 Chi tiết Phòng

- Ảnh/banner phòng trên cùng, chip trạng thái nổi trên ảnh.
- Thông tin cơ bản: diện tích, giá thuê, dịch vụ kèm theo (điện/nước/internet...).
- Tab con: Thông tin | Hợp đồng | Hóa đơn | Người thuê.
- Nút hành động chính theo ngữ cảnh: "Tạo hợp đồng mới" (nếu trống) hoặc "Xem hợp đồng hiện tại" (nếu đang thuê).

### 8.4 Danh sách Người thuê

- Danh sách 1 cột: avatar, tên, phòng đang thuê, trạng thái hợp đồng (chip).
- Tìm kiếm theo tên/SĐT.

### 8.5 Hồ sơ Người thuê

- Thông tin liên hệ, CMND/CCCD (ẩn một phần, có nút hiện đầy đủ).
- Lịch sử hóa đơn & thanh toán.
- Hợp đồng hiện tại + nút xem chi tiết hợp đồng (PDF/file đính kèm nếu có).

### 8.6 Danh sách Hóa đơn

- Bộ lọc chip: Tất cả / Chưa thanh toán / Đã thanh toán / Quá hạn, cuộn ngang dưới header.
- Mỗi hàng: card hóa đơn như mô tả mục 7, có thể vuốt trái để hiện hành động nhanh (Đánh dấu đã thanh toán / Nhắc thanh toán).
- FAB "+ Tạo hóa đơn".

### 8.7 Chi tiết Hóa đơn

- Số tiền tổng lớn, nổi bật trên cùng cùng chip trạng thái.
- Bảng chi tiết các khoản: tiền phòng, điện, nước, dịch vụ khác — mỗi dòng có nhãn + số tiền căn phải.
- Lịch sử thanh toán (nếu thanh toán nhiều lần/đặt cọc).
- Nút hành động: "Đánh dấu đã thanh toán", "Gửi nhắc nhở", "Chia sẻ hóa đơn".

### 8.8 Thông báo

- Danh sách theo thời gian, nhóm "Hôm nay" / "Trước đó".
- Icon phân loại theo nguồn (hóa đơn, sự cố, hợp đồng, hệ thống).
- Chưa đọc: chấm tròn nhỏ màu `color-brand` cạnh tiêu đề.

### 8.9 Phản ánh sự cố

- Danh sách sự cố với chip trạng thái: Mới / Đang xử lý / Đã xử lý.
- Form gửi sự cố mới: chọn phòng (nếu chủ trọ) hoặc tự động gắn phòng của mình (nếu người thuê), loại sự cố, mô tả, đính kèm ảnh.
- Chi tiết sự cố: mô tả, ảnh đính kèm, lịch sử cập nhật trạng thái dạng timeline dọc.

---

## 9. Form nhập liệu

### Nguyên tắc bố cục

- Mỗi form là **1 cột**, label nằm trên field (không inline bên trái) để tối ưu cho màn hình hẹp.
- Khoảng cách giữa các field: `space-3` (12px) trong cùng nhóm, `space-5` (20px) giữa các nhóm field.
- Field bắt buộc: dấu `*` đỏ nhỏ cạnh label, không dùng riêng màu nền để đánh dấu bắt buộc.
- Trường số tiền/diện tích: bàn phím numeric, định dạng phân cách nghìn tự động khi nhập.
- Trường ngày (hạn hợp đồng, kỳ hóa đơn): mở date picker dạng bottom sheet, không dùng input gõ tay.
- Trường chọn phòng/người thuê: mở danh sách chọn dạng bottom sheet có tìm kiếm, không dùng dropdown thu nhỏ.

### Validate & lỗi

- Validate khi rời field (on blur), không validate khi đang gõ để tránh giật.
- Thông báo lỗi ngắn gọn, cụ thể (VD: "Số tiền phải lớn hơn 0"), hiển thị ngay dưới field, màu `color-error`.
- Nút "Lưu" ở cuối form chỉ bật khi các field bắt buộc hợp lệ; có thể vẫn để bật nhưng hiện lỗi khi bấm — chọn 1 cách nhất quán toàn app.

### Form đa bước (VD tạo hợp đồng)

- Dùng progress indicator dạng chấm hoặc thanh ngang mỏng trên cùng form, không dùng số bước phức tạp.
- Cho phép quay lại bước trước mà không mất dữ liệu đã nhập.

---

## 10. Bảng / Danh sách dữ liệu

Trên mobile, "bảng" được thể hiện dưới dạng **danh sách hàng (row list)** thay vì bảng nhiều cột kiểu desktop.

### Cấu trúc hàng chuẩn

```
[Icon/Avatar]  Tiêu đề chính (H3, 1 dòng, ellipsis nếu dài)        [Giá trị chính / Chip trạng thái]
               Mô tả phụ (Body Small, màu secondary)                [Mốc thời gian phụ - Caption]
```

- Cao tối thiểu 64px để đủ chạm và chứa 2 dòng text.
- Viền phân cách mỏng `color-border` 1px giữa các hàng, hoặc cách nhau `space-2` nếu dùng dạng card rời.
- Hàng có thể vuốt (swipe) để lộ hành động nhanh (đánh dấu thanh toán, gọi điện, xóa) — hành động phá hủy luôn nằm bên phải, màu `color-error`.

### Sắp xếp & lọc

- Bộ lọc trạng thái luôn ở dạng **chip cuộn ngang** ngay dưới header, không dùng dropdown ẩn.
- Sắp xếp (mới nhất, hạn gần nhất, số tiền) đặt trong icon "lọc/sắp xếp" ở header, mở bottom sheet lựa chọn.

### Phân trang

- Dùng **infinite scroll** với skeleton loading khi tải thêm, không dùng nút "Trang sau" kiểu desktop.

---

## 11. Trạng thái dữ liệu (Status)

Thống nhất một bộ trạng thái dùng xuyên suốt app, mỗi trạng thái có **chip màu nền nhạt + chữ đậm cùng tông**:

### Trạng thái phòng

| Trạng thái | Nền | Chữ |
|------------|-----|-----|
| Trống | `#E6FFFA` | `#0EA5A4` |
| Đang thuê | `#E8F1FD` | `#2F6FED` |
| Đang sửa chữa | `#FEF3C7` | `#92400E` |

### Trạng thái hóa đơn

| Trạng thái | Nền | Chữ |
|------------|-----|-----|
| Đã thanh toán | `#DCFCE7` | `#15803D` |
| Chưa thanh toán | `#F1F2F4` | `#4B5563` |
| Quá hạn | `#FEE2E2` | `#B91C1C` |

### Trạng thái hợp đồng

| Trạng thái | Nền | Chữ |
|------------|-----|-----|
| Còn hiệu lực | `#DCFCE7` | `#15803D` |
| Sắp hết hạn (≤30 ngày) | `#FEF3C7` | `#92400E` |
| Đã hết hạn / Đã thanh lý | `#F1F2F4` | `#4B5563` |

### Trạng thái sự cố

| Trạng thái | Nền | Chữ |
|------------|-----|-----|
| Mới | `#E8F1FD` | `#2F6FED` |
| Đang xử lý | `#FEF3C7` | `#92400E` |
| Đã xử lý | `#DCFCE7` | `#15803D` |

**Quy tắc:** không tạo thêm trạng thái màu ngoài bộ trên; nếu cần trạng thái mới, ánh xạ về nhóm gần nhất (thành công/cảnh báo/lỗi/trung tính/thông tin).

---

## 12. Empty State

Mỗi danh sách rỗng cần có:

- Icon hoặc minh họa đơn giản, kích thước vừa phải (khoảng 80–120px), màu trung tính nhạt — không dùng ảnh chụp thật.
- Tiêu đề ngắn gọn (H3): VD "Chưa có phòng nào".
- Mô tả phụ 1 dòng (Body Small, màu secondary): VD "Thêm phòng đầu tiên để bắt đầu quản lý".
- Nút hành động chính (Primary button) nếu phù hợp ngữ cảnh: VD "+ Thêm phòng".

Áp dụng nhất quán cho: danh sách phòng trống, hóa đơn rỗng (lọc không có kết quả), thông báo rỗng, sự cố rỗng, kết quả tìm kiếm rỗng.

Trường hợp **lọc không có kết quả** (khác với rỗng hoàn toàn): thông điệp đổi thành "Không tìm thấy kết quả phù hợp" + nút "Xóa bộ lọc" thay vì nút thêm mới.

---

## 13. Loading State

- **Skeleton screen** cho danh sách/card đang tải lần đầu — hình chữ nhật bo góc xám nhạt (`color-border` shimmer), giữ đúng layout của nội dung thật để tránh giật khi load xong.
- **Pull-to-refresh** cho mọi danh sách chính (phòng, hóa đơn, thông báo).
- **Inline spinner** trong nút khi submit form (giữ nguyên kích thước nút, thay icon/text bằng spinner nhỏ).
- **Infinite scroll loading**: hiển thị spinner nhỏ căn giữa cuối danh sách khi tải thêm trang.
- Không dùng full-screen loading che toàn bộ giao diện trừ khi đang chuyển màn hình lần đầu (cold start).

---

## 14. Animation & Tương tác

- Thời lượng chuyển động ngắn: 150–200ms cho hầu hết transition (mở sheet, chuyển tab, hiện/ẩn chip) — đủ mượt nhưng không làm chậm thao tác.
- Chuyển trang: trượt ngang chuẩn của nền tảng (iOS: slide từ phải; Android: theo Material motion mặc định).
- Bottom sheet: trượt lên từ dưới, có thể vuốt xuống để đóng.
- Hiệu ứng nhấn (pressed state): giảm độ mờ 5–8% hoặc scale nhẹ 0.98 cho card/button.
- Hành động thành công (VD đánh dấu đã thanh toán): hiệu ứng check ngắn + chip trạng thái đổi màu tức thì, kèm toast xác nhận nhỏ ở dưới cùng màn hình (tự ẩn sau ~2s).
- Không dùng animation trang trí không phục vụ mục đích (không có hiệu ứng nảy, không có particle, không có hiệu ứng chuyển cảnh cầu kỳ) — đây là công cụ quản lý, ưu tiên tốc độ cảm nhận.

---

## 15. Việc nên làm / không nên làm

### Nên làm

- Luôn hiển thị trạng thái (phòng/hóa đơn/hợp đồng/sự cố) bằng chip màu nhất quán theo mục 11.
- Số tiền luôn đậm, căn phải trong danh sách, có đơn vị "đ".
- Dùng bottom sheet cho mọi lựa chọn cần tìm kiếm (chọn phòng, chọn người thuê, chọn ngày).
- Giữ margin ngang 16px nhất quán toàn app.
- Cho phép pull-to-refresh và vuốt hành động nhanh trên mọi danh sách chính.

### Không nên làm

- Không dùng nhiều hơn 1 màu nhấn chính trên cùng một màn hình.
- Không hạ body text xuống dưới 13px.
- Không dùng dropdown thu nhỏ kiểu desktop cho lựa chọn quan trọng (ngày, phòng, người thuê) trên mobile.
- Không dùng `color-error` cho bất kỳ mục đích nào ngoài lỗi/quá hạn/hành động phá hủy.
- Không thêm animation trang trí làm chậm cảm nhận thao tác của chủ trọ/người thuê khi đang xử lý hóa đơn hoặc sự cố gấp.
