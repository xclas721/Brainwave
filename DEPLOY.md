# main 部署到 Zeabur（後端）

1. Zeabur → Add Service → 從 GitHub 選**此 repo**（後端）。
2. Root Directory 維持**根目錄**（本 repo 根即專案根）。
3. **環境變數**（程式會吃這些，在服務 Variables 設定）：
   - `SPRING_PROFILES_ACTIVE` = `prod`
   - `DB_URL`、`DB_USERNAME`、`DB_PASSWORD`（必填，資料庫連線）
   - 其餘見 `brainwave-backend/.env.example`。
4. 部署完成後取得後端網址，給前端 Vercel 的 `VITE_API_BASE_URL` 使用。

確認 main 能部署成功後，再開 develop 分支。
