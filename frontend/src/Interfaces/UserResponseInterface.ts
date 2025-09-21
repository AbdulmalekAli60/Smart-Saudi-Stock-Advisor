export default interface UserResponseInterface {
  message: string | undefined;
  name: string | undefined;
  username: string | undefined;
  email: string | undefined;
  joinDate: string | undefined;
  userId: number | undefined;
  role: string | undefined;

  investAmount: number;
}
