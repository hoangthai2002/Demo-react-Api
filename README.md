Tổng kết về JWT
1. Quá trình tạo ra/mã hóa Token (encode)
Khi nào tạo ra token: người dùng login thành công, server cần trả ra token gửi cho client.
Tất cả các request sau này gửi lên server truy cập API, client cần truyền lên token này để định danh (xác thực)
Quá trình encode gồm các bước:
JWT = header.payload.signature
Phần header bao gồm thông tin về thuật toán mã hóa => khai báo thuật toán
Phần payload là data truyền theo token (được dùng để định danh người dùng)
Phần signature là chữ ký, được tạo ra bằng cách : 
JWK (key) + thuật toán +  ký vào (header + payload) 
=> với Java Spring, cần cấu hình  JwtEncoder, khai báo Key + thuật toán 
   @Bean
    public JwtEncoder jwtEncoder() { … }
//todo: giải thích code
2. Quá trình giải mã Token (decode)
Client muốn truy cập & sử dụng API, cần truyền lên Token (đã có từ bước login) tại header của Request, thông thường là dạng Bearer you-token-here
Bước 1: client gửi kèm JWT token ở header request
Bước 2: Tại phía Server của Spring (sau khi đã cấu hình oauth2-resource-server), sẽ kích hoạt filter BearerTokenAuthenticationFilter
Filter này sẽ “tự động tách” Bear Token (bạn không cần phải làm thủ công, thư viện đã làm sẵn rồi)
Token sẽ được xử lý tiếp, thông qua:
JwtDecoder : giải mã token (check tính hợp lệ của token)
Check như thế nào :  
JWT = header.payload.signature
Decoder sẽ tách header, payload, đồng thời lấy Key + thuật toán để băm ngược ra signature
Nếu 2 signature là trùng nhau => token hợp lệ. 
Quá trình này tương tự việc so sánh mật khẩu khi login (băm mật khẩu thành hash để so sánh với database, nếu trùng nhau, tức là nhập đúng thông tin)
JwtAuthenticationConverter  : convert data chứa trong token, lưu  vào Spring Security Context để reuse



