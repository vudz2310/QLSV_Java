

CREATE DATABASE QLYSV2
GO

USE QLYSV2
GO

IF EXISTS(SELECT name FROM sysobjects WHERE name='tblSinhVien')
DROP TABLE tblSinhVien
GO

CREATE TABLE tblSinhVien 
(
    MaSV NVarChar(50) NOT NULL, 
    HoTen NVarChar(50) NOT NULL, 
    NgaySinh Date NOT NULL, 
    GioiTinh NVarChar(5) NOT NULL, 
    DiaChi NVarChar(150) NOT NULL, 
    DienThoai Varchar(20),
    CONSTRAINT PK_SinhVien PRIMARY KEY(MaSV)
)
GO
INSERT INTO tblSinhVien(MaSV, HoTen, NgaySinh, GioiTinh, DiaChi, DienThoai) 
VALUES 
('SV01', N'Trần Nguyên Hương', '03/03/2000', N'Nam', N'Hà Nội', '0988180955'),
('SV02', N'Nguyễn Phương Dung', '16/08/2001', N'Nữ', N'Thái Nguyên', '0981234566'),
('SV03', N'Trần Văn Ước', '01/03/2001', N'Nam', N'Nam Định', '0989876789'),
('SV04', N'Hoàng Thu Phương', '03/01/2002', N'Nữ', N'Hà Nội', '0985635679'),
('SV05', N'Trương Thị Thu Hà', '19/05/2000', N'Nữ', N'Nghệ An', '0985676554'),
('SV06', N'Phạm Văn Tuấn', '15/02/2000', N'Nam', N'Quảng Bình', '0989876543'),
('SV07', N'Lê Thị Mai', '24/12/2000', N'Nữ', N'Hải Phòng', '0981239874'),
('SV08', N'Trần Đức Anh', '07/07/2000', N'Nam', N'Thanh Hóa', '0989873212'),
('SV09', N'Hoàng Quốc Việt', '09/09/2000', N'Nam', N'Bắc Ninh', '0986547893'),
('SV10', N'Nguyễn Văn Cường', '11/11/2000', N'Nam', N'Hà Nam', '0985671234');

-- 2. Tạo bảng tblMonHoc
IF EXISTS(SELECT name FROM sysobjects WHERE name='tblMonHoc')
DROP TABLE tblMonHoc
GO

CREATE TABLE tblMonHoc
(
    MaMH NVarChar(50) NOT NULL,
    TenMonHoc NVarChar(50) NOT NULL, 
    SoTinChi Int NOT NULL,
    CONSTRAINT PK_MonHoc PRIMARY KEY(MaMH)
)
GO

-- 3. Tạo bảng tblDangKy
IF EXISTS(SELECT name FROM sysobjects WHERE name='tblDangKy')
DROP TABLE tblDangKy
GO

CREATE TABLE tblDangKy
(
    MaDK NVarChar(20) NOT NULL, 
    MaSV NVarChar(50) NOT NULL,
    MaMH NVarChar(50) NOT NULL,
    NgayDangKy Date, 
    CONSTRAINT PK_DangKy PRIMARY KEY(MaDK),
    CONSTRAINT FK_DangKy_SinhVien FOREIGN KEY(MaSV) REFERENCES tblSinhVien(MaSV),
    CONSTRAINT FK_DangKy_MonHoc FOREIGN KEY(MaMH) REFERENCES tblMonHoc(MaMH)
)
GO

-- 4. Tạo bảng tblKetQuaHocTap
IF EXISTS(SELECT name FROM sysobjects WHERE name='tblKetQuaHocTap')
DROP TABLE tblKetQuaHocTap
GO

CREATE TABLE tblKetQuaHocTap
(
    MaDK NVarChar(20) NOT NULL,
    Diem Float NOT NULL,
    CONSTRAINT PK_KetQuaHocTap PRIMARY KEY(MaDK),
    CONSTRAINT FK_KetQuaHocTap_DangKy FOREIGN KEY(MaDK) REFERENCES tblDangKy(MaDK)
)
GO

-- 5. Tạo bảng TaiKhoan
IF EXISTS(SELECT name FROM sysobjects WHERE name='TaiKhoan')
DROP TABLE TaiKhoan
GO

CREATE TABLE TaiKhoan
(
    TenDN NVarChar(20) NOT NULL,
    MatKhau NVarChar(50) NOT NULL, 
    CONSTRAINT PK_TaiKhoan PRIMARY KEY(TenDN)
)
GO

-- --------- CHÈN DỮ LIỆU ---------

-- 1. Chèn dữ liệu vào bảng tblSinhVien
SET DateFormat DMY
GO




-- 2. Chèn dữ liệu vào bảng tblMonHoc
INSERT INTO tblMonHoc(MaMH, TenMonHoc, SoTinChi) 
VALUES 
('MH01', N'Toán cao cấp', 3),
('MH02', N'Lập trình C++', 4),
('MH03', N'Triết học Mác-Lênin', 2),
('MH04', N'Vật lý đại cương', 3),
('MH05', N'Giải tích', 3);
GO

-- 3. Chèn dữ liệu vào bảng tblDangKy
SET dateformat DMY 
GO

INSERT INTO tblDangKy(MaDK, MaSV, MaMH, NgayDangKy) 
VALUES 
('DK001', 'SV01', 'MH01', '01/09/2023'),
('DK002', 'SV02', 'MH02', '02/09/2023'),
('DK003', 'SV01', 'MH03', '01/09/2023'),
('DK004', 'SV03', 'MH04', '12/09/2023'),
('DK005', 'SV04', 'MH05', '01/09/2023'),
('DK006', 'SV06', 'MH01', '02/09/2023'),
('DK007', 'SV07', 'MH02', '02/09/2023'),
('DK008', 'SV08', 'MH03', '03/09/2023'),
('DK009', 'SV09', 'MH04', '05/09/2023'),
('DK010', 'SV10', 'MH05', '05/09/2023');
GO

-- 4. Chèn dữ liệu vào bảng tblKetQuaHocTap
INSERT INTO tblKetQuaHocTap(MaDK, Diem) 
VALUES 
('DK001', 8.5),
('DK002', 7.0),
('DK003', 6.5),
('DK004', 9.0),
('DK005', 7.5),
('DK006', 8.0),
('DK007', 7.5),
('DK008', 9.0),
('DK009', 6.5),
('DK010', 7.0);
GO

-- 5. Chèn dữ liệu vào bảng TaiKhoan
INSERT INTO TaiKhoan(TenDN, MatKhau) 
VALUES 
('admin', 'admin'),
('loanhtuan', '123456');
GO

-- --------- TRUY VẤN DỮ LIỆU ---------

-- Truy vấn thông tin sinh viên
SELECT * FROM tblSinhVien;
GO

-- Truy vấn môn học
SELECT * FROM tblMonHoc;
GO

-- Truy vấn thông tin đăng ký môn học của sinh viên
SELECT * FROM tblDangKy;
GO

-- Truy vấn kết quả học tập
SELECT * FROM tblKetQuaHocTap;
GO

-- Truy vấn tài khoản người dùng
SELECT * FROM TaiKhoan;
GO
