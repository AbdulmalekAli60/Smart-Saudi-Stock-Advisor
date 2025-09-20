import { useState } from "react";
import Input from "../components/Input";
import { useUserInfo } from "../contexts/UserContext";

export default function AccountPage() {
  const [updateData, setUpdateData] = useState<boolean>(false);

  const { currentUserData } = useUserInfo();

  const isUpdateData = updateData ? false : true;

  function handleInputChange(e: React.ChangeEvent<HTMLInputElement>) {
    console.log(e);
  }

  return (
    <main className="max-w-4/5 flex-col justify-between items-center h-screen bg-red-700 m-auto p-2">
      <div className="bg-yellow-300 w-full h-2/5">
        <label>الإسم</label>
        <Input
          id="text"
          isDisabled={isUpdateData}
          isRequired={false}
          name="name"
          type="text"
          onChange={handleInputChange}
          value={currentUserData.name}
        />

        <label>إسم المستخدم</label>
        <Input
          id="text"
          isDisabled={isUpdateData}
          isRequired={false}
          name="username"
          type="text"
          onChange={handleInputChange}
          value={currentUserData.username}
        />

        <label>الايميل</label>
        <Input
          id="text"
          isDisabled={isUpdateData}
          isRequired={false}
          name="email"
          type="text"
          onChange={handleInputChange}
          value={currentUserData.email}
        />

        <label>الرقم السري</label>
        <Input
          id="text"
          isDisabled={isUpdateData}
          isRequired={false}
          name="password"
          type="text"
          onChange={handleInputChange}
          value={currentUserData.email}
        />

        <label>تاريخ التسجيل</label>
        <span>{currentUserData.joinDate}</span>

        <br />
        <button
          className="bg-red-400 cursor-pointer"
          onClick={() => {
            setUpdateData(!updateData);
          }}
        >
          {isUpdateData ? "تغيير البيانات" : "حفظ"}
        </button>
      </div>

      <section className="bg-green-400 w-full h-3/5 ">
        <div className="bg-accent max-w-4/5 h-full ">s</div>
      </section>
    </main>
  );
}
